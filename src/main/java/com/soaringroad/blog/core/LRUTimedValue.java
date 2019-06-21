package com.soaringroad.blog.core;

import lombok.Data;

/**
 * LRU缓存用的值，带有创建时间，用于检验是否过期
 * 
 * @author wangzhenhui1992
 */
@Data
public class LRUTimedValue<V> {
  private long createdTime;
  private V value;
  public long getCreatedTime() {
    return createdTime;
  }
  public void setCreatedTime(long createdTime) {
    this.createdTime = createdTime;
  }
  public V getValue() {
    return value;
  }
  public void setValue(V value) {
    this.value = value;
  }
  
}
