package com.damai.filter;


import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.conf.RequestTemporaryWrapper;
import com.damai.enums.BaseCode;
import com.damai.exception.ArgumentError;
import com.damai.exception.ArgumentException;
import com.damai.exception.DaMaiFrameException;
import com.damai.pro.limit.RateLimiter;
import com.damai.pro.limit.RateLimiterProperty;
import com.damai.property.GatewayProperty;
import com.damai.service.ApiRestrictService;
import com.damai.service.ChannelDataService;
import com.damai.service.TokenService;
import com.damai.threadlocal.BaseParameterHolder;
import com.damai.util.RsaSignTool;
import com.damai.util.RsaTool;
import com.damai.util.StringUtil;
import com.damai.vo.GetChannelDataVo;
import com.damai.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.damai.constant.Constant.GRAY_PARAMETER;
import static com.damai.constant.Constant.TRACE_ID;
import static com.damai.constant.GatewayConstant.BUSINESS_BODY;
import static com.damai.constant.GatewayConstant.CODE;
import static com.damai.constant.GatewayConstant.ENCRYPT;
import static com.damai.constant.GatewayConstant.NO_VERIFY;
import static com.damai.constant.GatewayConstant.REQUEST_BODY;
import static com.damai.constant.GatewayConstant.TOKEN;
import static com.damai.constant.GatewayConstant.USER_ID;
import static com.damai.constant.GatewayConstant.V2;
import static com.damai.constant.GatewayConstant.VERIFY_VALUE;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 请求过滤器
 * @author: 阿星不是程序员
 **/

@Component
@Slf4j
public class RequestValidationFilter implements GlobalFilter, Ordered {

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    @Autowired
    private ChannelDataService channelDataService;

    @Autowired
    private ApiRestrictService apiRestrictService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private GatewayProperty gatewayProperty;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private RateLimiterProperty rateLimiterProperty;
    
    @Autowired
    private RateLimiter rateLimiter;
    

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        if (rateLimiterProperty.getRateSwitch()) {
            try {
                rateLimiter.acquire();
                return doFilter(exchange,chain);
            } catch (InterruptedException e) {
                log.error("interrupted error",e);
                throw new DaMaiFrameException(BaseCode.THREAD_INTERRUPTED);
            } finally {
                rateLimiter.release();
            }
        }else{
            return doFilter(exchange, chain);
        }
    }
    
    public Mono<Void> doFilter(final ServerWebExchange exchange, final GatewayFilterChain chain){
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_ID);
        String gray = request.getHeaders().getFirst(GRAY_PARAMETER);
        String noVerify = request.getHeaders().getFirst(NO_VERIFY);
        if (StringUtil.isEmpty(traceId)) {
            traceId = String.valueOf(uidGenerator.getUid());
        }
        MDC.put(TRACE_ID,traceId);
        Map<String,String> headMap = new HashMap<>(8);
        headMap.put(TRACE_ID,traceId);
        headMap.put(GRAY_PARAMETER,gray);
        if (StringUtil.isNotEmpty(noVerify)) {
            headMap.put(NO_VERIFY,noVerify);
        }
        BaseParameterHolder.setParameter(TRACE_ID,traceId);
        BaseParameterHolder.setParameter(GRAY_PARAMETER,gray);
        MediaType contentType = request.getHeaders().getContentType();
        //application json请求
        if (Objects.nonNull(contentType) && contentType.toString().toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE.toLowerCase())) {
            return readBody(exchange,chain,headMap);
        }else {
            Map<String, String> map = doExecute("", exchange);
            map.remove(REQUEST_BODY);
            map.putAll(headMap);
            request.mutate().headers(httpHeaders -> {
                map.forEach(httpHeaders::add);
            });
            return chain.filter(exchange);
        }
    } 

    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain, Map<String,String> headMap){
        log.info("current thread readBody : {}",Thread.currentThread().getName());
        RequestTemporaryWrapper requestTemporaryWrapper = new RequestTemporaryWrapper();
        
        ServerRequest serverRequest = ServerRequest.create(exchange, serverCodecConfigurer.getReaders());
        Mono<String> modifiedBody = serverRequest
                .bodyToMono(String.class)
                .flatMap(originalBody -> Mono.just(execute(requestTemporaryWrapper,originalBody,exchange)))
                .switchIfEmpty(Mono.defer(() -> Mono.just(execute(requestTemporaryWrapper,"",exchange))));
        
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter
                .insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> chain.filter(
                        exchange.mutate().request(decorateHead(exchange, headers, outputMessage, requestTemporaryWrapper, headMap)).build()
                )))
                .onErrorResume((Function<Throwable, Mono<Void>>) throwable -> Mono.error(throwable));
    }
    
    public String execute(RequestTemporaryWrapper requestTemporaryWrapper,String requestBody,ServerWebExchange exchange){
        //进行业务验证，并将相关参数放入map
        Map<String, String> map = doExecute(requestBody, exchange);
        String body = map.get(REQUEST_BODY);
        map.remove(REQUEST_BODY);
        requestTemporaryWrapper.setMap(map);
        return body;
    }

    private Map<String,String> doExecute(String originalBody,ServerWebExchange exchange){
        ServerHttpRequest request = exchange.getRequest();
        String requestBody = originalBody;
        Map<String, String> bodyContent = new HashMap<>(32);
        if (StringUtil.isNotEmpty(originalBody)) {
            bodyContent = JSON.parseObject(originalBody, Map.class);
        }
        String code = null;
        String token;   
        String userId = null;
        String url = request.getPath().value();
        String noVerify = request.getHeaders().getFirst(NO_VERIFY);
        boolean allowNormalAccess = gatewayProperty.isAllowNormalAccess();
        if ((!allowNormalAccess) && (VERIFY_VALUE.equals(noVerify))) {
            throw new DaMaiFrameException(BaseCode.ONLY_SIGNATURE_ACCESS_IS_ALLOWED);
        }
        if (checkParameter(originalBody,noVerify) && !skipCheckParameter(url)) {

            String encrypt = request.getHeaders().getFirst(ENCRYPT);
            //应用渠道
            code = bodyContent.get(CODE);
            //token
            token = request.getHeaders().getFirst(TOKEN);
            
            GetChannelDataVo channelDataVo = channelDataService.getChannelDataByCode(code);
            
            if (StringUtil.isNotEmpty(encrypt) && V2.equals(encrypt)) {
                String decrypt = RsaTool.decrypt(bodyContent.get(BUSINESS_BODY),channelDataVo.getDataSecretKey());
                bodyContent.put(BUSINESS_BODY,decrypt);
            }
            boolean checkFlag = RsaSignTool.verifyRsaSign256(bodyContent, channelDataVo.getSignPublicKey());
            if (!checkFlag) {
                throw new DaMaiFrameException(BaseCode.RSA_SIGN_ERROR);
            }

            boolean skipCheckTokenResult = skipCheckToken(url);
            if (!skipCheckTokenResult && StringUtil.isEmpty(token)) {
                ArgumentError argumentError = new ArgumentError();
                argumentError.setArgumentName(token);
                argumentError.setMessage("token参数为空");
                List<ArgumentError> argumentErrorList = new ArrayList<>();
                argumentErrorList.add(argumentError);
                throw new ArgumentException(BaseCode.ARGUMENT_EMPTY.getCode(),argumentErrorList);
            }

            if (!skipCheckTokenResult) {
                UserVo userVo = tokenService.getUser(token,code,channelDataVo.getTokenSecret());
                userId = userVo.getId();
            }
            
            if (StringUtil.isEmpty(userId) && checkNeedUserId(url) && StringUtil.isNotEmpty(token)) {
                UserVo userVo = tokenService.getUser(token,code,channelDataVo.getTokenSecret());
                userId = userVo.getId();
            }
            
            requestBody = bodyContent.get(BUSINESS_BODY);
        }
        apiRestrictService.apiRestrict(userId,url,request);
        Map<String,String> map = new HashMap<>(4);
        map.put(REQUEST_BODY,requestBody);
        if (StringUtil.isNotEmpty(code)) {
            map.put(CODE,code);
        }
        if (StringUtil.isNotEmpty(userId)) {
            map.put(USER_ID,userId);
        }
        return map;
    }
    /**
     * 将网关层request请求头中的重要参数传递给后续的微服务中
     */
    private ServerHttpRequestDecorator decorateHead(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage, RequestTemporaryWrapper requestTemporaryWrapper, Map<String,String> headMap){
        return new ServerHttpRequestDecorator(exchange.getRequest()){
            @Override
            public HttpHeaders getHeaders() {
                log.info("current thread getHeaders: {}",Thread.currentThread().getName());
                long contentLength = headers.getContentLength();
                HttpHeaders newHeaders = new HttpHeaders();
                newHeaders.putAll(headers);
                Map<String, String> map = requestTemporaryWrapper.getMap();
                if (CollectionUtil.isNotEmpty(map)) {
                    newHeaders.setAll(map);
                }
                if (CollectionUtil.isNotEmpty(headMap)) {
                    newHeaders.setAll(headMap);
                }
                if (contentLength > 0){
                    newHeaders.setContentLength(contentLength);
                }else {
                    newHeaders.set(HttpHeaders.TRANSFER_ENCODING,"chunked");
                }
                if (CollectionUtil.isNotEmpty(headMap) && StringUtil.isNotEmpty(headMap.get(TRACE_ID))) {
                    MDC.put(TRACE_ID,headMap.get(TRACE_ID));
                }
                return newHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    @Override
    public int getOrder() {
        return -2;
    }

    public boolean skipCheckToken(String url){
        for (String skipCheckTokenPath : gatewayProperty.getCheckTokenPaths()) {
            PathMatcher matcher = new AntPathMatcher();
            if (matcher.match(skipCheckTokenPath, url)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean skipCheckParameter(String url){
        for (String skipCheckTokenPath : gatewayProperty.getCheckSkipParmeterPaths()) {
            PathMatcher matcher = new AntPathMatcher();
            if (matcher.match(skipCheckTokenPath, url)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkParameter(String originalBody,String noVerify){
        return (!(VERIFY_VALUE.equals(noVerify))) && StringUtil.isNotEmpty(originalBody);
    }
    
    private boolean checkNeedUserId(String url){
        for (String userIdPath : gatewayProperty.getUserIdPaths()) {
            PathMatcher matcher = new AntPathMatcher();
            if (matcher.match(userIdPath, url)) {
                return true;
            }
        }
        return false;
    }
}
