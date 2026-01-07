/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.damai.log4j;

import com.damai.util.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;

import java.io.IOException;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.damai.constant.Constant.TRACE_ID;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: JSON 日志布局基类
 * @author: 阿星不是程序员
 **/
abstract class BaseJsonLayout extends AbstractStringLayout {

    private static final String LINE_SEPARATOR = "\r\n";
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static volatile String cachedLocalIp;

    protected final ObjectWriter jsonWriter;
    protected final String lineSeparator;
    protected final boolean outputComplete;
    protected final String projectName;
    protected final String env;

    protected BaseJsonLayout(Configuration config, ObjectWriter jsonWriter, Charset charset,
                             boolean compact, boolean complete, boolean appendNewline,
                             Serializer headerSerializer, Serializer footerSerializer,
                             String projectName, String env) {
        super(config, charset, headerSerializer, footerSerializer);
        this.jsonWriter = jsonWriter;
        this.outputComplete = complete;
        this.lineSeparator = (compact && !appendNewline) ? Strings.EMPTY : LINE_SEPARATOR;
        this.projectName = StringUtil.isNotEmpty(projectName) ? projectName : Strings.EMPTY;
        this.env = StringUtil.isNotEmpty(env) ? env : Strings.EMPTY;
    }

    @Override
    public String toSerializable(LogEvent event) {
        try (StringBuilderWriter sw = new StringBuilderWriter()) {
            toSerializable(event, sw);
            return sw.toString();
        } catch (IOException e) {
            LOGGER.error("序列化日志失败", e);
            return Strings.EMPTY;
        }
    }

    /**
     * 将日志写入 Writer
     */
    public void toSerializable(LogEvent event, Writer out) throws IOException {
        // 先用 Jackson 序列化原始日志事件
        LogEvent safeEvent = toImmutableEvent(event);
        String rawJson = jsonWriter.writeValueAsString(safeEvent);

        // 构建增强后的日志对象
        Map<String, Object> logData = buildEnhancedLogData(rawJson);

        // 输出最终 JSON
        jsonWriter.writeValue(out, logData);
        out.write(lineSeparator);
        markEvent();
    }

    /**
     * 构建增强后的日志数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildEnhancedLogData(String rawJson) throws IOException {
        Map<String, Object> logData = new LinkedHashMap<>();
        
        // 添加项目标识
        logData.put("projectName", projectName);
        if (StringUtil.isNotEmpty(env)) {
            logData.put("env", env);
        }

        // 合并原始日志数据
        Map<String, Object> originalData = JSON_MAPPER.readValue(rawJson, LinkedHashMap.class);
        logData.putAll(originalData);

        // 添加链路追踪信息
        appendTraceInfo(logData);

        // 添加服务器信息
        logData.put("localIp", resolveLocalIp());

        return logData;
    }

    /**
     * 添加链路追踪信息
     */
    private void appendTraceInfo(Map<String, Object> logData) {
        String traceId = MDC.get(TRACE_ID);
        if (StringUtil.isNotEmpty(traceId)) {
            logData.put("traceId", traceId);
        }
    }

    /**
     * 获取本机 IP 地址
     */
    private String resolveLocalIp() {
        if (cachedLocalIp != null) {
            return cachedLocalIp;
        }
        synchronized (BaseJsonLayout.class) {
            if (cachedLocalIp == null) {
                cachedLocalIp = detectLocalIp();
            }
        }
        return cachedLocalIp;
    }

    /**
     * 探测本机 IP
     */
    private String detectLocalIp() {
        // 优先用 Spring Cloud 的工具
        String ip = trySpringCloudInetUtils();
        if (ip != null) {
            return ip;
        }
        // 降级到网卡遍历
        return scanNetworkInterfaces();
    }

    private String trySpringCloudInetUtils() {
        try {
            Class<?> propsClass = Class.forName("org.springframework.cloud.commons.util.InetUtilsProperties");
            Class<?> utilsClass = Class.forName("org.springframework.cloud.commons.util.InetUtils");
            Object props = propsClass.getDeclaredConstructor().newInstance();
            Object utils = utilsClass.getDeclaredConstructor(propsClass).newInstance(props);
            Object hostInfo = utilsClass.getMethod("findFirstNonLoopbackHostInfo").invoke(utils);
            return (String) hostInfo.getClass().getMethod("getIpAddress").invoke(hostInfo);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String scanNetworkInterfaces() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isLoopback() || ni.isVirtual() || !ni.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }

    /**
     * 转换为不可变事件对象
     */
    private LogEvent toImmutableEvent(LogEvent event) {
        return (event instanceof MutableLogEvent) 
                ? ((MutableLogEvent) event).createMemento() 
                : event;
    }
}
