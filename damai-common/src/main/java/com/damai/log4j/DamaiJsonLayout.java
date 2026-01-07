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

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 大麦项目自定义 JSON 日志布局
 * @author: 阿星不是程序员
 **/
@Plugin(name = "DamaiJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class DamaiJsonLayout extends BaseJsonLayout {

    private static final String JSON_ARRAY_START = "[";
    private static final String JSON_ARRAY_END = "]";
    private static final String MIME_TYPE = "application/json";

    private DamaiJsonLayout(Configuration config, boolean locationInfo, boolean properties,
                            boolean threadContextAsList, boolean complete, boolean compact,
                            boolean eventEol, String header, String footer, Charset charset,
                            boolean includeStacktrace, String projectName, String env,
                            boolean stacktraceAsString, boolean objectMessageAsJson) {
        super(config,
                JsonWriterBuilder.create()
                        .threadContextAsList(threadContextAsList)
                        .includeStacktrace(includeStacktrace)
                        .stacktraceAsString(stacktraceAsString)
                        .objectMessageAsJson(objectMessageAsJson)
                        .showLocation(locationInfo)
                        .showProperties(properties)
                        .compactMode(compact)
                        .build(),
                charset, compact, complete, eventEol,
                createSerializer(config, header, JSON_ARRAY_START),
                createSerializer(config, footer, JSON_ARRAY_END),
                projectName, env);
    }

    private static Serializer createSerializer(Configuration config, String pattern, String defaultPattern) {
        return PatternLayout.newSerializerBuilder()
                .setConfiguration(config)
                .setPattern(pattern)
                .setDefaultPattern(defaultPattern)
                .setAlwaysWriteExceptions(false)
                .setNoConsoleNoAnsi(false)
                .build();
    }

    @Override
    public byte[] getHeader() {
        if (!outputComplete) {
            return null;
        }
        String headerStr = serializeToString(getHeaderSerializer());
        return getBytes((headerStr != null ? headerStr : Strings.EMPTY) + lineSeparator);
    }

    @Override
    public byte[] getFooter() {
        if (!outputComplete) {
            return null;
        }
        String footerStr = serializeToString(getFooterSerializer());
        return getBytes(lineSeparator + (footerStr != null ? footerStr : Strings.EMPTY) + lineSeparator);
    }

    @Override
    public Map<String, String> getContentFormat() {
        Map<String, String> format = new HashMap<>(1);
        format.put("version", "2.0");
        return format;
    }

    @Override
    public String getContentType() {
        return MIME_TYPE + "; charset=" + getCharset();
    }

    @Override
    public void toSerializable(LogEvent event, Writer writer) throws IOException {
        if (outputComplete && eventCount > 0) {
            writer.append(", ");
        }
        super.toSerializable(event, writer);
    }

    /**
     * 创建布局实例
     */
    @PluginFactory
    public static DamaiJsonLayout newLayout(
            @PluginConfiguration Configuration config,
            @PluginAttribute(value = "locationInfo", defaultBoolean = false) boolean locationInfo,
            @PluginAttribute(value = "properties", defaultBoolean = false) boolean properties,
            @PluginAttribute(value = "propertiesAsList", defaultBoolean = false) boolean propertiesAsList,
            @PluginAttribute(value = "complete", defaultBoolean = false) boolean complete,
            @PluginAttribute(value = "compact", defaultBoolean = false) boolean compact,
            @PluginAttribute(value = "eventEol", defaultBoolean = false) boolean eventEol,
            @PluginAttribute(value = "header", defaultString = JSON_ARRAY_START) String header,
            @PluginAttribute(value = "footer", defaultString = JSON_ARRAY_END) String footer,
            @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset,
            @PluginAttribute(value = "includeStacktrace", defaultBoolean = true) boolean includeStacktrace,
            @PluginAttribute(value = "projectName", defaultString = "") String projectName,
            @PluginAttribute(value = "env", defaultString = "") String env,
            @PluginAttribute(value = "stacktraceAsString", defaultBoolean = true) boolean stacktraceAsString,
            @PluginAttribute(value = "objectMessageAsJsonObject", defaultBoolean = true) boolean objectMessageAsJson
    ) {
        boolean threadContextAsList = properties && propertiesAsList;
        return new DamaiJsonLayout(config, locationInfo, properties, threadContextAsList,
                complete, compact, eventEol, header, footer, charset, includeStacktrace,
                projectName, env, stacktraceAsString, objectMessageAsJson);
    }

    /**
     * 创建默认实例（用于测试）
     */
    public static DamaiJsonLayout defaultLayout() {
        return new DamaiJsonLayout(new DefaultConfiguration(), false, false, false,
                false, false, false, JSON_ARRAY_START, JSON_ARRAY_END, StandardCharsets.UTF_8,
                true, Strings.EMPTY, Strings.EMPTY, true, true);
    }
}
