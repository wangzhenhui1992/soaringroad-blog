package com.soaringroad.blog.core;

import java.util.LinkedHashMap;

/**
 * LRU缓存
 * 
 * @author wangzhenhui1992
 */
public class LRUCache<K,V> extends LinkedHashMap<K, LRUTimedValue<V>> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private int cacheSize;
  private long duration;

  public LRUCache(int cacheSize) {
    super(16, 0.75f, true);
    this.cacheSize = cacheSize;
  }

  @Override
  public LRUTimedValue<V> get(Object key) {
    LRUTimedValue<V> value = super.get(key);
    if (value == null) {
      return null;
    }
    if (value.getCreatedTime() + duration < System.currentTimeMillis()) {
      this.remove(key);
      return null;
    }
    ;
    return value;
  }
  
  public LRUTimedValue<V> set(K key, V value) {
    LRUTimedValue<V> timedValue = new LRUTimedValue<V>();
    timedValue.setValue(value);
    timedValue.setCreatedTime(System.currentTimeMillis());
    return this.put(key, timedValue);
  }

  @Override
  protected boolean removeEldestEntry(java.util.Map.Entry<K, LRUTimedValue<V>> eldest) {
    return this.size() > cacheSize;
  }

}
