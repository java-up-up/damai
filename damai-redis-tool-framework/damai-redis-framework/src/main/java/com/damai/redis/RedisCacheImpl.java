package com.damai.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.damai.util.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis方法实现
 * @author: 阿星不是程序员
 **/
@AllArgsConstructor
public class RedisCacheImpl implements RedisCache {
    
    private StringRedisTemplate redisTemplate;

    @Override
    public <T> T get(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        // 如果取String类型 则直接取出返回
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (String.class.isAssignableFrom(clazz)) {
            return (T) cachedValue;
        }
        return getComplex(cachedValue, clazz);
    }
    
    @Override
    public <T> T get(RedisKeyBuild redisKeyBuild, Class<T> clazz, Supplier<T> supplier, long ttl, TimeUnit timeUnit) {
        T t = get(redisKeyBuild, clazz);
        if (CacheUtil.isEmpty(t)) {
            t = supplier.get();
            if (CacheUtil.isEmpty(t)) {
                return null;
            }
            set(redisKeyBuild,t,ttl,timeUnit);
        }
        return t;
    }

    @Override
    public String getRange(RedisKeyBuild redisKeyBuild, long start, long end) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForValue().get(key, start, end);
    };

    @Override
    public <T> List<T> getValueIsList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String valueStr = redisTemplate.opsForValue().get(key);
        if (StringUtil.isEmpty(valueStr)) {
            return new ArrayList<>();
        }
        return JSON.parseArray(valueStr, clazz);
    }
    
    @Override
    public <T> List<T> getValueIsList(RedisKeyBuild redisKeyBuild, Class<T> clazz, Supplier<List<T>> supplier, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String valueStr = redisTemplate.opsForValue().get(key);
        List<T> tList = null;
        if (CacheUtil.isEmpty(valueStr)) {
            tList = supplier.get();
            if (CacheUtil.isEmpty(tList)) {
                return null;
            }
            set(redisKeyBuild,tList,ttl,timeUnit);
        }
        return tList;
    }


    @Override
    public List<String> getKeys(List<RedisKeyBuild> keyList) {
        CacheUtil.checkNotEmpty(keyList);
        List<String> batchKey = CacheUtil.getBatchKey(keyList);
        List<String> list = redisTemplate.opsForValue().multiGet(batchKey);
        
        return CacheUtil.optimizeRedisList(redisTemplate.opsForValue().multiGet(CacheUtil.optimizeRedisList(list)));
    }

    @Override
    public Boolean hasKey(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long getExpire(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.getExpire(key);
    }
    
    @Override
    public Long getExpire(RedisKeyBuild redisKeyBuild,TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.getExpire(key,timeUnit);
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public Boolean move(RedisKeyBuild redisKeyBuild, int dbIndex) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.move(key, dbIndex);
    }

    @Override
    public Boolean persist(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.persist(key);
    }

    @Override
    public String randomKey() {
        return redisTemplate.randomKey();
    }

    @Override
    public void rename(RedisKeyBuild oldKey, RedisKeyBuild newKey) {
        CacheUtil.checkNotBlank(oldKey);
        CacheUtil.checkNotBlank(newKey);
        redisTemplate.rename(oldKey.getRelKey(), newKey.getRelKey());
    }

    @Override
    public Boolean renameIfAbsent(RedisKeyBuild oldKey, RedisKeyBuild newKey) {
        CacheUtil.checkNotBlank(oldKey);
        CacheUtil.checkNotBlank(newKey);
        return redisTemplate.renameIfAbsent(oldKey.getRelKey(), newKey.getRelKey());
    }

    @Override
    public DataType type(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.type(key);
    }
    
    @Override
    public void set(RedisKeyBuild redisKeyBuild, Object object) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public void set(RedisKeyBuild redisKeyBuild, Object object, long ttl) {
        set(redisKeyBuild, object, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void set(RedisKeyBuild redisKeyBuild, Object object, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        redisTemplate.opsForValue().set(key, json, ttl, timeUnit);
    }

    @Override
    public boolean setIfAbsent(RedisKeyBuild redisKeyBuild, Object object) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String json = object instanceof String ? (String) object : JSON.toJSONString(object);
        return redisTemplate.opsForValue().setIfAbsent(key, json);
    }

    @Override
    public Long size(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForValue().size(key);
    }

    @Override
    public void multiSet(Map<RedisKeyBuild, ?> map) {
        CacheUtil.checkNotEmpty(map);
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey.getRelKey(), jsonValue);
        });
        redisTemplate.opsForValue().multiSet(mapForSave);
    }

    @Override
    public boolean multiSetIfAbsent(Map<RedisKeyBuild, ?> map) {
        CacheUtil.checkNotEmpty(map);
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey.getRelKey(), jsonValue);
        });
        return redisTemplate.opsForValue().multiSetIfAbsent(mapForSave);
    }

    @Override
    public Long incrBy(RedisKeyBuild redisKeyBuild, long increment) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public Double incrByDouble(RedisKeyBuild redisKeyBuild, double increment) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForValue().increment(key, increment);
    }

    @Override
    public Integer append(RedisKeyBuild redisKeyBuild, String value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForValue().append(key, value);
    }

    @Override
    public void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForHash().put(key, hashKey, jsonValue);
    }

    @Override
    public void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value, long ttl) {
        putHash(redisKeyBuild, hashKey, value, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void putHash(RedisKeyBuild redisKeyBuild, String hashKey, Object value, long ttl, TimeUnit timeUnit) {
        putHash(redisKeyBuild, hashKey, value);
        // 设置过期时间
        expire(redisKeyBuild, ttl, timeUnit);
    }

    @Override
    public void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Map<String, String> mapForSave = new HashMap<>(map.size());
        map.forEach((hashKey, val) -> {
            String jsonValue = val instanceof String ? (String) val : JSON.toJSONString(val);
            mapForSave.put(hashKey, jsonValue);
        });
        redisTemplate.opsForHash().putAll(key, mapForSave);
    }

    @Override
    public void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map, long ttl) {
        putHash(redisKeyBuild, map, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void putHash(RedisKeyBuild redisKeyBuild, Map<String, ?> map, long ttl, TimeUnit timeUnit) {
        putHash(redisKeyBuild, map);
        expire(redisKeyBuild, ttl, timeUnit);
    }

    @Override
    public Boolean putHashIfAbsent(RedisKeyBuild redisKeyBuild, String hashKey, Object value){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForHash().putIfAbsent(key, hashKey, jsonValue);
    }

    @Override
    public <T> T getForHash(RedisKeyBuild redisKeyBuild, String hashKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        Object o = redisTemplate.opsForHash().get(key, hashKey);
        // 如果取String类型 则直接取出返回
        if (String.class.isAssignableFrom(clazz)) {
            return (T) o;
        }
        return getComplex(o, clazz);
    }

    @Override
    public <T> List<T> getValueIsListForHash(RedisKeyBuild redisKeyBuild, String hashKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        Object o = redisTemplate.opsForHash().get(key,hashKey);
        if (o == null) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        if (o instanceof String) {
            list = JSON.parseArray((String)o, clazz);
        }
        return list;
    }

    @Override
    public <T> List<T> multiGetForHash(RedisKeyBuild redisKeyBuild, List<String> hashKeys, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKeys);
        String key = redisKeyBuild.getRelKey();
        List<Object> objHashKeys = new ArrayList<>(hashKeys);
        List<Object> multiGetObj = redisTemplate.opsForHash().multiGet(key, objHashKeys);
        
        if (CacheUtil.checkRedisListIsEmpty(multiGetObj)){
            return new ArrayList<>();
        }
        if (String.class.isAssignableFrom(clazz)) {
            return (List<T>) multiGetObj;
        }

        return parseObjects(multiGetObj, clazz);
    }

    @Override
    public <T> List<T> getAllForHash(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List<Object> valuesObj = redisTemplate.opsForHash().values(key);
        if (CacheUtil.checkRedisListIsEmpty(valuesObj)){
            return new ArrayList<>();
        }
        if (String.class.isAssignableFrom(clazz)) {
            return (List<T>) valuesObj;
        }

        return parseObjects(valuesObj, clazz);
    }
    
    @Override
    public <T> Map<String,T> getAllMapForHash(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<String,T> map = new HashMap<>(64);
        entries.forEach((k,v) -> {
            map.put(String.valueOf(k),getComplex(v, clazz));
        });
        return map;
    }

    @Override
    public <T> T indexForList(RedisKeyBuild redisKeyBuild, long index, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String cachedValue = redisTemplate.opsForList().index(key, index);
        if (StringUtil.isEmpty(cachedValue)) {
            return null;
        }
        if (String.class.isAssignableFrom(clazz)) {
            return (T) cachedValue;
        }
        return getComplex(cachedValue, clazz);
    }

    @Override
    public Long leftPushForList(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPush(key, jsonValue);
    }

    @Override
    public Long leftPushAllForList(RedisKeyBuild redisKeyBuild, List<?> valueList){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(valueList);
        String key = redisKeyBuild.getRelKey();
        List<String> jsonList = new ArrayList<>(valueList.size());
        valueList.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForList().leftPushAll(key, jsonList);
    }

    @Override
    public Long leftPushIfPresentForList(RedisKeyBuild redisKeyBuild, Object value){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPushIfPresent(key, jsonValue);
    }

    @Override
    public Long leftPushForList(RedisKeyBuild redisKeyBuild, Object pivot, Object value){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(pivot);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonPivot = value instanceof String ? (String) pivot : JSON.toJSONString(pivot);
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().leftPush(key, jsonPivot, jsonValue);
    }

    @Override
    public Long rightPushForList(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPush(key, jsonValue);
    }

    @Override
    public Long rightPushAllForList(RedisKeyBuild redisKeyBuild, List<Object> valueList){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(valueList);
        String key = redisKeyBuild.getRelKey();
        List<String> jsonList = new ArrayList<>(valueList.size());
        valueList.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForList().rightPushAll(key, jsonList);
    }

    @Override
    public Long rightPushIfPresentForList(RedisKeyBuild redisKeyBuild, Object value){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPushIfPresent(key, jsonValue);
    }

    @Override
    public Long rightPushForList(RedisKeyBuild redisKeyBuild, Object pivot, Object value){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(pivot);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonPivot = value instanceof String ? (String) pivot : JSON.toJSONString(pivot);
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().rightPush(key, jsonPivot, jsonValue);
    }

    @Override
    public void setForList(RedisKeyBuild redisKeyBuild, long index, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForList().set(key, index, jsonValue);
    }

    @Override
    public <T> T leftPopForList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String s = redisTemplate.opsForList().leftPop(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T leftPopBlockForList(RedisKeyBuild redisKeyBuild, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String s = redisTemplate.opsForList().leftPop(key, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopForList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String s = redisTemplate.opsForList().rightPop(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopBlockForList(RedisKeyBuild redisKeyBuild, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String s = redisTemplate.opsForList().rightPop(key, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopAndLeftPushForList(RedisKeyBuild sourceKey, RedisKeyBuild destinationKey, Class<T> clazz) {
        CacheUtil.checkNotBlank(sourceKey);
        CacheUtil.checkNotBlank(destinationKey);
        String sourceRelKey = sourceKey.getRelKey();
        String destinationRelKey = destinationKey.getRelKey();
        String s = redisTemplate.opsForList().rightPopAndLeftPush(sourceRelKey, destinationRelKey);
        return getComplex(s, clazz);
    }

    @Override
    public <T> T rightPopBlockAndLeftPushForList(RedisKeyBuild sourceKey, RedisKeyBuild destinationKey, Class<T> clazz, long timeout, TimeUnit unit) {
        CacheUtil.checkNotBlank(sourceKey);
        CacheUtil.checkNotBlank(destinationKey);
        String sourceRelKey = sourceKey.getRelKey();
        String destinationRelKey = destinationKey.getRelKey();
        String s = redisTemplate.opsForList().rightPopAndLeftPush(sourceRelKey, destinationRelKey, timeout, unit);
        return getComplex(s, clazz);
    }

    @Override
    public <T> List<T> getAllForList(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List list = redisTemplate.opsForList().range(key, 0, -1);
        if (CacheUtil.checkRedisListIsEmpty(list)){
            return new ArrayList<>();
        }
        return parseObjects(list, clazz);
    }

    @Override
    public <T> List<T> rangeForList(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz){
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List range = redisTemplate.opsForList().range(key, start, end);
        if (CacheUtil.checkRedisListIsEmpty(range)){
            return new ArrayList<>();
        }
        return parseObjects(range, clazz);
    }

    @Override
    public Long removeForList(RedisKeyBuild redisKeyBuild, long index, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForList().remove(key, index, jsonValue);
    }

    @Override
    public void trimForList(RedisKeyBuild redisKeyBuild, long start, long end) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        redisTemplate.opsForList().trim(key, start, end);
    }

    @Override
    public Long lenForList(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Boolean hasKeyForHash(RedisKeyBuild redisKeyBuild, String hashKey) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    @Override
    public void del(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        redisTemplate.delete(key);
    }
    

    @Override
    public Long delForHash(RedisKeyBuild redisKeyBuild, String hashKey) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForHash().delete(key, hashKey);
    }

    @Override
    public Long delForHash(RedisKeyBuild redisKeyBuild, Collection<String> hashKeys) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKeys);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForHash().delete(key, hashKeys.toArray());
    }

    @Override
    public Long incrByForHash(RedisKeyBuild redisKeyBuild, String hashKey, long increment) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForHash().increment(key, hashKey, increment);
    }

    @Override
    public Double incrByDoubleForHash(RedisKeyBuild redisKeyBuild, String hashKey, double delta){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(hashKey);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public Set<String> hashKeysForHash(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        return parseObjects(keys,String.class);
    }

    @Override
    public Long sizeForHash(RedisKeyBuild redisKeyBuild){
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public void del(Collection<RedisKeyBuild> keys) {
        CacheUtil.checkNotEmpty(keys);
        List<String> batchKey = CacheUtil.getBatchKey(keys);
        redisTemplate.delete(batchKey);
    }

    @Override
    public Boolean expire(RedisKeyBuild redisKeyBuild, long ttl, TimeUnit timeUnit) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.expire(key, ttl, timeUnit);
    }

    @Override
    public Long addForSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().add(key, jsonValue);
    }

    @Override
    public Long addForSet(RedisKeyBuild redisKeyBuild, List<?> values) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(values);
        String key = redisKeyBuild.getRelKey();
        List<String> jsonList = new ArrayList<>(values.size());
        values.forEach(value -> {
            String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
            jsonList.add(jsonValue);
        });
        return redisTemplate.opsForSet().add(key, jsonList.toArray(new String[]{}));
    }

    @Override
    public Long removeForSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().remove(key, jsonValue);
    }

    @Override
    public Long removeForSet(RedisKeyBuild redisKeyBuild, List<?> values) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(values);
        String key = redisKeyBuild.getRelKey();
        List<String> jsonList = values.stream()
                .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                .collect(Collectors.toList());
        return redisTemplate.opsForSet().remove(key, jsonList.toArray(new String[]{}));
    }

    @Override
    public <T> T popForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String cachedValue = redisTemplate.opsForSet().pop(key);
        return getComplex(cachedValue,clazz);
    }

    @Override
    public boolean moveForSet(RedisKeyBuild redisKeyBuild, Object value, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String destKey = destRedisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().move(key, jsonValue, destKey);
    }

    @Override
    public Long sizeForSet(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Boolean isMemberForSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForSet().isMember(key, jsonValue);
    }

    @Override
    public <T> Set<T> intersectForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForSet().intersect(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> intersectForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        Set set = redisTemplate.opsForSet().intersect(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long intersectAndStoreForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long intersectAndStoreForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> unionForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForSet().union(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> unionForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        Set set = redisTemplate.opsForSet().union(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long unionAndStoreForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long unionAndStoreForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> differenceForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForSet().difference(key, otherKey);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<T> differenceForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        Set set = redisTemplate.opsForSet().difference(key, otherKeys);
        return parseObjects(set,clazz);
    }

    @Override
    public Long differenceForSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().differenceAndStore(key, otherKey, destKey);
    }

    @Override
    public Long differenceForSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForSet().differenceAndStore(key, otherKeys, destKey);
    }

    @Override
    public <T> Set<T> membersForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set members = redisTemplate.opsForSet().members(key);
        return parseObjects(members,clazz);
    }

    @Override
    public <T> T randomMemberForSet(RedisKeyBuild redisKeyBuild, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String s = redisTemplate.opsForSet().randomMember(key);
        return getComplex(s, clazz);
    }

    @Override
    public <T> List<T> randomMembersForSet(RedisKeyBuild redisKeyBuild, long count, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List list = redisTemplate.opsForSet().randomMembers(key, count);
        if (CacheUtil.checkRedisListIsEmpty(list)){
            return new ArrayList<>();
        }
        return parseObjects(list,clazz);
    }

    @Override
    public <T> Set<T> distinctRandomMembersForSet(RedisKeyBuild redisKeyBuild, long count, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForSet().distinctRandomMembers(key, count);
        return parseObjects(set,clazz);
    }

    @Override
    public Cursor<String> scanForSet(RedisKeyBuild redisKeyBuild, ScanOptions options) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Cursor<String> scan = redisTemplate.opsForSet().scan(key, options);
        return scan;
    }

    @Override
    public void addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, Double score){
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        CacheUtil.checkNotEmpty(score);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        redisTemplate.opsForZSet().add(key,jsonValue,score);
    }

    @Override
    public void addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, Double score, long ttl){
        addForSortedSet(redisKeyBuild,value,score,ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public void addForSortedSet(RedisKeyBuild redisKeyBuild, Object value, Double score, long ttl, TimeUnit timeUnit){
        addForSortedSet(redisKeyBuild,value,score);
        expire(redisKeyBuild, ttl, timeUnit);
    }

    @Override
    public Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> collect =
                map.entrySet()
                        .stream()
                        .map(item -> {
                            String entryKey = item.getKey() instanceof String ? (String) item.getKey() : JSON.toJSONString(item.getKey());
                            return new DefaultTypedTuple<>(entryKey, item.getValue());
                        })
                        .collect(Collectors.toSet());
        return redisTemplate.opsForZSet().add(key, collect);
    }

    @Override
    public Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map, long ttl) {
        return addForSortedSet(redisKeyBuild, map, ttl, CacheUtil.DEFAULT_TIME_UNIT);
    }

    @Override
    public Long addForSortedSet(RedisKeyBuild redisKeyBuild, Map<?, Double> map, long ttl, TimeUnit timeUnit) {
        Long count = addForSortedSet(redisKeyBuild, map);
        expire(redisKeyBuild, ttl, timeUnit);
        return count;
    }

    @Override
    public <T> Set<T> getRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set resultSet = redisTemplate.opsForZSet().range(key, start, end);
        return parseObjects(resultSet,clazz);
    }

    @Override
    public <T> Set<T> getReverseRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set resultSet = redisTemplate.opsForZSet().reverseRange(key, start, end);
        return parseObjects(resultSet,clazz);
    }

    @Override
    public Long delForSortedSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().remove(key, jsonValue);
    }

    @Override
    public Long delForSortedSet(RedisKeyBuild redisKeyBuild, Collection<?> valueCollection) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(valueCollection);
        String key = redisKeyBuild.getRelKey();
        List<String> jsonValueList = valueCollection.stream()
                .map(value -> value instanceof String ? (String) value : JSON.toJSONString(value))
                .distinct()
                .collect(Collectors.toList());
        return redisTemplate.opsForZSet().remove(key,jsonValueList.toArray());
    }

    @Override
    public Long delRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Double incrementScoreForSortedSet(RedisKeyBuild redisKeyBuild, Object value, double delta) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().incrementScore(key, jsonValue, delta);
    }

    @Override
    public Long sizeForSortedSet(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Long rankForSortedSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().rank(key, jsonValue);
    }

    @Override
    public Long reverseRankForSortedSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().reverseRank(key, jsonValue);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        if (cacheSet == null) {
            return new HashSet<>();
        }
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> rangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> rangeByScoreWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max,
                                                                                   long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> reverseRangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
        return parseObjects(set,clazz);
    }

    @Override
    public <T> Set<ZSetOperations.TypedTuple<T>> reverseRangeByScoreWithScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set<ZSetOperations.TypedTuple<String>> cacheSet = redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max);
        return typedTupleStringParseObjects(cacheSet, clazz);
    }

    @Override
    public <T> Set<T> reverseRangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max, long start, long end, Class<T> clazz) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        Set set = redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end);
        return parseObjects(set, clazz);
    }

    @Override
    public Long countForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    @Override
    public Long zCardForSortedSet(RedisKeyBuild redisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().zCard(key);
    }

    @Override
    public Double scoreByValueForSortedSet(RedisKeyBuild redisKeyBuild, Object value) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(value);
        String key = redisKeyBuild.getRelKey();
        String jsonValue = value instanceof String ? (String) value : JSON.toJSONString(value);
        return redisTemplate.opsForZSet().score(key, jsonValue);
    }

    @Override
    public Long removeRangeForSortedSet(RedisKeyBuild redisKeyBuild, long start, long end) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public Long removeRangeByScoreForSortedSet(RedisKeyBuild redisKeyBuild, double min, double max) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    @Override
    public Long unionAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey);
    }

    @Override
    public Long unionAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey);
    }

    @Override
    public Long intersectAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, RedisKeyBuild otherRedisKeyBuild, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotBlank(otherRedisKeyBuild);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        String otherKey = otherRedisKeyBuild.getRelKey();
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey);
    }

    @Override
    public Long intersectAndStoreForSortedSet(RedisKeyBuild redisKeyBuild, Collection<RedisKeyBuild> otherRedisKeyBuilds, RedisKeyBuild destRedisKeyBuild) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        CacheUtil.checkNotEmpty(otherRedisKeyBuilds);
        CacheUtil.checkNotBlank(destRedisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        List<String> otherKeys = CacheUtil.getBatchKey(otherRedisKeyBuilds);
        String destKey = destRedisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey);
    }

    @Override
    public Cursor<ZSetOperations.TypedTuple<String>> scanForSortedSet(RedisKeyBuild redisKeyBuild, ScanOptions options) {
        CacheUtil.checkNotBlank(redisKeyBuild);
        String key = redisKeyBuild.getRelKey();
        return redisTemplate.opsForZSet().scan(key, options);
    }

    @Override
    public <T> T getByType(RedisKeyBuild redisKeyBuild, Type genericReturnType){
        String key = redisKeyBuild.getRelKey();
        String s = redisTemplate.boundValueOps(key).get();
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        return JSONObject.parseObject(s ,genericReturnType);
    }

    @Override
    public RedisTemplate getInstance() {
        return redisTemplate;
    }


    public <T> T getComplex(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        if (clazz.isAssignableFrom(String.class)) {
            if (source instanceof String) {
                return (T)source;
            }else{
                return (T)JSON.toJSONString(source);
            }
        }
        return source instanceof String ? JSON.parseObject((String) source, CacheUtil.buildType(clazz)) : null;
    }

    public <T> List<T> parseObjects(List<Object> sources, Class<T> clazz) {
        if (sources == null) {
            return new ArrayList<>();
        }
        if (clazz.isAssignableFrom(String.class)) {
            List<T> resultList = (List<T>) sources.stream()
                    .filter(Objects::nonNull)
                    .map(each -> each instanceof String ? (String) each : JSON.toJSONString(each))
                    .collect(Collectors.toList());
            return resultList;
        }
        List<T> resultList = (List<T>) sources.stream()
                .filter(Objects::nonNull)
                .map(each -> each instanceof String ? JSON.parseObject((String) each, CacheUtil.buildType(clazz)) : null)
                .collect(Collectors.toList());
        return resultList;
    }

    public <T> Set<T> parseObjects(Set<Object> sources, Class<T> clazz) {
        if (sources == null) {
            return new HashSet<>();
        }
        if (clazz.isAssignableFrom(String.class)) {
            Set<T> resultSet = (Set<T>) sources.stream()
                    .map(each -> each instanceof String ? (String) each : JSON.toJSONString(each))
                    .collect(Collectors.toSet());
            return resultSet;
        }
        Set<T> resultSet = (Set<T>) sources.stream()
                .map(each -> each instanceof String ? JSON.parseObject((String) each, CacheUtil.buildType(clazz)) : null)
                .collect(Collectors.toSet());
        return resultSet;
    }

    public <T> Set<ZSetOperations.TypedTuple<T>> typedTupleStringParseObjects(Set<ZSetOperations.TypedTuple<String>> sources, Class<T> clazz){
        if (sources == null) {
            return new HashSet<>();
        }
        Set<ZSetOperations.TypedTuple<T>> set = new HashSet<>(sources.size());
        for (ZSetOperations.TypedTuple<String> typedTuple : sources) {
            String value = typedTuple.getValue();
            T complex = getComplex(value, clazz);
            Double score = typedTuple.getScore();
            DefaultTypedTuple defaultTypedTuple = new DefaultTypedTuple(complex,score);
            set.add(defaultTypedTuple);
        }
        return set;
    }

    public <T> Set<ZSetOperations.TypedTuple<T>> typedTupleParseObjects(Set<ZSetOperations.TypedTuple> sources, Class<T> clazz){
        if (sources == null) {
            return new HashSet<>();
        }
        Set<ZSetOperations.TypedTuple<T>> set = new HashSet<>(sources.size());
        for (ZSetOperations.TypedTuple typedTuple : sources) {
            Object value = typedTuple.getValue();
            T complex = getComplex(value, clazz);
            Double score = typedTuple.getScore();
            DefaultTypedTuple defaultTypedTuple = new DefaultTypedTuple(complex,score);
            set.add(defaultTypedTuple);
        }
        return set;
    }
}
