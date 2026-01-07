package com.damai.log4j;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;

import java.util.HashSet;
import java.util.Set;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: JSON ObjectWriter 构建器
 * @author: 阿星不是程序员
 **/
public class JsonWriterBuilder {

    private boolean threadContextAsList;
    private boolean includeStacktrace = true;
    private boolean stacktraceAsString = true;
    private boolean objectMessageAsJson = true;
    private boolean showLocation;
    private boolean showProperties;
    private boolean compactMode;

    public static JsonWriterBuilder create() {
        return new JsonWriterBuilder();
    }

    public JsonWriterBuilder threadContextAsList(boolean value) {
        this.threadContextAsList = value;
        return this;
    }

    public JsonWriterBuilder includeStacktrace(boolean value) {
        this.includeStacktrace = value;
        return this;
    }

    public JsonWriterBuilder stacktraceAsString(boolean value) {
        this.stacktraceAsString = value;
        return this;
    }

    public JsonWriterBuilder objectMessageAsJson(boolean value) {
        this.objectMessageAsJson = value;
        return this;
    }

    public JsonWriterBuilder showLocation(boolean value) {
        this.showLocation = value;
        return this;
    }

    public JsonWriterBuilder showProperties(boolean value) {
        this.showProperties = value;
        return this;
    }

    public JsonWriterBuilder compactMode(boolean value) {
        this.compactMode = value;
        return this;
    }

    public ObjectWriter build() {
        // 构建过滤器
        Set<String> excludeFields = new HashSet<>(3);
        if (!showLocation) {
            excludeFields.add(JsonConstants.ELT_SOURCE);
        }
        if (!showProperties) {
            excludeFields.add(JsonConstants.ELT_CONTEXT_MAP);
        }
        excludeFields.add(JsonConstants.ELT_NANO_TIME);

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter(Log4jLogEvent.class.getName(), 
                SimpleBeanPropertyFilter.serializeAllExcept(excludeFields));

        // 构建 ObjectMapper 和 Writer
        Log4jJsonObjectMapper mapper = new Log4jJsonObjectMapper(
                threadContextAsList, includeStacktrace, stacktraceAsString, objectMessageAsJson);

        return mapper.writer(compactMode ? new MinimalPrettyPrinter() : new DefaultPrettyPrinter())
                .with(filterProvider);
    }
}
