package com.soaringroad.blog.core;

import lombok.Data;

/**
 * LRU缓存用的值，带有创建时间，用于检验是否过期
 * 
 * @author wangzhenhui1992
 */
@Data
public class TimedValue<V> {
  private volatile long expireDuration = -1;
  private volatile V value;
  private volatile long expireTime = -1;
}
