package com.soaringroad.blog.core;

import lombok.Data;

/**
 * 请求上下文
 * 
 * @author wangzhenhui1992
 */
@Data
public class RequestContext {
  private String requestId;
  private String securityKey;
}
