package com.damai.controller;

import com.damai.common.ApiResponse;
import com.damai.dto.AccountOrderCountDto;
import com.damai.dto.OrderCancelDto;
import com.damai.dto.OrderCreateDto;
import com.damai.dto.OrderGetDto;
import com.damai.dto.OrderListDto;
import com.damai.dto.OrderPayCheckDto;
import com.damai.dto.OrderPayDto;
import com.damai.service.OrderService;
import com.damai.vo.AccountOrderCountVo;
import com.damai.vo.OrderGetVo;
import com.damai.vo.OrderListVo;
import com.damai.vo.OrderPayCheckVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单 控制层
 * @author: 阿星不是程序员
 **/
@RestController
@RequestMapping("/order")
@Api(tags = "order", value = "订单")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @ApiOperation(value = "订单创建(不提供给前端调用，只允许内部program服务调用)")
    @PostMapping(value = "/create")
    public ApiResponse<String> create(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        return ApiResponse.ok(orderService.create(orderCreateDto));
    }
    
    @ApiOperation(value = "订单支付")
    @PostMapping(value = "/pay")
    public ApiResponse<String> pay(@Valid @RequestBody OrderPayDto orderPayDto) {
        return ApiResponse.ok(orderService.pay(orderPayDto));
    }
    
    @ApiOperation(value = "订单支付后状态检查")
    @PostMapping(value = "/pay/check")
    public ApiResponse<OrderPayCheckVo> payCheck(@Valid @RequestBody OrderPayCheckDto orderPayCheckDto) {
        return ApiResponse.ok(orderService.payCheck(orderPayCheckDto));
    }
    
    @ApiOperation(value = "支付宝支付后回调通知")
    @PostMapping(value = "/alipay/notify")
    public String alipayNotify(HttpServletRequest request) {
        return orderService.alipayNotify(request);
    }
    
    @ApiOperation(value = "查看订单列表")
    @PostMapping(value = "/select/list")
    public ApiResponse<List<OrderListVo>> selectList(@Valid @RequestBody OrderListDto orderListDto) {
        return ApiResponse.ok(orderService.selectList(orderListDto));
    }
    
    @ApiOperation(value = "查看订单详情")
    @PostMapping(value = "/get")
    public ApiResponse<OrderGetVo> get(@Valid @RequestBody OrderGetDto orderGetDto) {
        return ApiResponse.ok(orderService.get(orderGetDto));
    }
    
    @ApiOperation(value = "账户下某个节目的订单数量(不提供给前端调用，只允许内部program服务调用)")
    @PostMapping(value = "/account/order/count")
    public ApiResponse<AccountOrderCountVo> accountOrderCount(@Valid @RequestBody AccountOrderCountDto accountOrderCountDto) {
        return ApiResponse.ok(orderService.accountOrderCount(accountOrderCountDto));
    }
    
    @ApiOperation(value = "查看缓存中的订单")
    @PostMapping(value = "/get/cache")
    public ApiResponse<String> getCache(@Valid @RequestBody OrderGetDto orderGetDto) {
        return ApiResponse.ok(orderService.getCache(orderGetDto));
    }
    
    @ApiOperation(value  = "订单详情取消")
    @PostMapping(value = "/cancel")
    public ApiResponse<Boolean> cancel(@Valid @RequestBody OrderCancelDto orderCancelDto) {
        return ApiResponse.ok(orderService.initiateCancel(orderCancelDto));
    }
}
