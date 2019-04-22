package com.soaringroad.blog.core;

import java.util.LinkedHashMap;
/**
 * LRU缓存
 * 
 * @author wangzhenhui1992
 */
public class LRUCache<V> extends LinkedHashMap<Long, LRUTimedValue<V>> {

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
      };
      return value;
    }



    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<Long, LRUTimedValue<V>> eldest) {
      return this.size() > cacheSize;
    }
    
}
