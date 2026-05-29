package com.damai.threadlocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 线程绑定工具
 * @author: 阿星不是程序员
 **/
public class BaseParameterHolder {
    
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL_MAP = new ThreadLocal<>();
    
    
    public static void setParameter(String name, String value) {
        Map<String, String> map = getParameterMap();
        map.put(name, value);
        THREAD_LOCAL_MAP.set(map);
    }
    
    public static String getParameter(String name) {
        return Optional.ofNullable(THREAD_LOCAL_MAP.get()).map(map -> map.get(name)).orElse(null);
    }
    
    public static void removeParameter(String name) {
        Map<String, String> map = THREAD_LOCAL_MAP.get();
        if (map != null) {
            map.remove(name);
        }
    }
    
    public static Map<String, String> getParameterMap() {
        Map<String, String> map = THREAD_LOCAL_MAP.get();
        if (map == null) {
            map = new HashMap<>(64);
        }
        return map;
    }
    
    public static void setParameterMap(Map<String, String> map) {
        THREAD_LOCAL_MAP.set(map);
    }
    
    public static void removeParameterMap(){
        THREAD_LOCAL_MAP.remove();
    }
}
