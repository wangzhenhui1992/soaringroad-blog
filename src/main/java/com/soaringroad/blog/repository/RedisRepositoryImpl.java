/******************************************************************
*   _____                  _             _____                 _  *
*  / ____|                (_)           |  __ \               | | *
* | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
*  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
*  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
* |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
*                                   __/ |                         *
*                                  |___/                          *
* Copyright ©2017-2018 www.soaringroad.com | All rights reserved. *
******************************************************************/
package com.soaringroad.blog.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisRepositoryImpl implements RedisRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object getValue(String key) {
        checkNull(key);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        checkNull(key);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public List<Object> getMultiValue(List<String> keys) {
        checkEmpty(keys);
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void setMultiValue(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    @Override
    public void increaseValue(String key, double delta) {
        checkNull(key);
        redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long increaseValue(String key, long delta) {
        checkNull(key);
        return redisTemplate.opsForValue().increment(key, delta);

    }

    @Override
    public Object getHashValue(String key, String hashKey) {
        checkNull(key);
        checkNull(hashKey);
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    @Override
    public void setHashValue(String key, String hashKey, Object value) {
        checkNull(key);
        checkNull(hashKey);
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public void setHashAll(String key, Map<String, Object> values) {
        checkNull(key);
        checkEmpty(values);
        redisTemplate.opsForHash().putAll(key, values);
    }

    @Override
    public void deleteHashValue(String key, String... hashKeys) {
        checkNull(key);
        checkEmpty(hashKeys);
        int length = hashKeys.length;
        Object[] objKeys = new Object[length];
        for (int i = 0; i < length; i++) {
            objKeys[i] = hashKeys[i];
        }
        redisTemplate.opsForHash().delete(key, objKeys);
    }

    @Override
    public void increaseHashValue(String key, String hashKey, long delta) {
        checkNull(key);
        checkNull(hashKey);
        redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public void increaseHashValue(String key, String hashKey, double delta) {
        checkNull(key);
        checkNull(hashKey);
        redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    @Override
    public void delete(String key) {
        checkNull(key);
        redisTemplate.delete(key);
    }

    @Override
    public void expire(String key, long seconds) {
        checkNull(key);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    private static void checkNull(String key) {
        Assert.notNull(key, "key must not be null");
    }

    private static void checkEmpty(String[] keys) {
        Assert.notEmpty(keys, "keys must not be empty");
    }

    private static void checkEmpty(Collection<String> keys) {
        Assert.notEmpty(keys, "keys must not be empty");
    }

    private static void checkEmpty(Map<String, Object> map) {
        Assert.notEmpty(map, "keys must not be empty");
    }

}
