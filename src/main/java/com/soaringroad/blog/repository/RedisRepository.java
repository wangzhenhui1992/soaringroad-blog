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

import java.util.List;
import java.util.Map;

public interface RedisRepository {

    Object getValue(String key);

    void setValue(String key, Object value);

    List<Object> getMultiValue(List<String> keys);

    void setMultiValue(Map<String, Object> map);

    void increaseValue(String key, double delta);

    Long increaseValue(String key, long delta);

    Object getHashValue(String key, String hashKey);

    void setHashValue(String key, String hashKey, Object value);

    void setHashAll(String key, Map<String, Object> values);

    void deleteHashValue(String key, String... hashKeys);

    void increaseHashValue(String key, String hashKey, long delta);

    void increaseHashValue(String key, String hashKey, double delta);

    void delete(String key);

    void expire(String key, long seconds);
}
