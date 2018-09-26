package com.soaringroad.blog.repository;

import java.util.List;
import java.util.Map;

public interface RedisRepository {

	Object getValue(String key);

	void setValue(String key, Object value);

	List<Object> getMultiValue(List<String> keys);

	void setMultiValue(Map<String, Object> map);

	void increaseValue(String key, double delta);

	void increaseValue(String key, long delta);

	Object getHashValue(String key, String hashKey);

	void setHashValue(String key, String hashKey, Object value);

	void setHashAll(String key, Map<String, Object> values);

	void deleteHashValue(String key, String... hashKeys);

	void increaseHashValue(String key, String hashKey, long delta);

	void increaseHashValue(String key, String hashKey, double delta);

}
