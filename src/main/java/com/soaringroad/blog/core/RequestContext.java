package com.soaringroad.blog.core;

import lombok.Data;

@Data
public class RequestContext {
  private String requestId;
  private String securityKey;
}
