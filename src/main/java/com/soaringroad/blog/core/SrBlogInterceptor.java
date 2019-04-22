package com.soaringroad.blog.core;

import com.alibaba.druid.util.StringUtils;
import com.soaringroad.blog.util.SrBlogConsts;
import org.jboss.logging.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SrBlogInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestId = request.getHeader(SrBlogConsts.REQUEST_ID);
    if (StringUtils.isEmpty(requestId)) {
      requestId = UUID.randomUUID().toString();
    }
    MDC.put(SrBlogConsts.REQUEST_ID, requestId);
    SrBlogContextContainer.setRequestId(requestId);
    SrBlogContextContainer.setSecurityKey(request.getHeader(HttpHeaders.AUTHORIZATION));
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                              Exception ex) throws Exception {
    super.afterCompletion(request, response, handler, ex);
  }

}
