package com.soaringroad.blog.core;

import com.alibaba.druid.util.StringUtils;
import com.soaringroad.blog.util.SrBlogConsts;
import org.jboss.logging.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
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
    MDC.remove(SrBlogConsts.REQUEST_ID);
    SrBlogContextContainer.setRequestContext(null);
    super.afterCompletion(request, response, handler, ex);
  }

}
