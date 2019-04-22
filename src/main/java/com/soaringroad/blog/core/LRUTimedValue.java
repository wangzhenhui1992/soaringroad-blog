package com.soaringroad.blog.core;

import lombok.Data;

@Data
public class LRUTimedValue<V> {
  private long createdTime;
  private V value;
}
