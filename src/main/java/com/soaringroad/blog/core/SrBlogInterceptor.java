package com.soaringroad.blog.core;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SrBlogInterceptor extends HandlerInterceptorAdapter {

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                              Exception ex) throws Exception {
    MDC.clear();
    SrBlogContextContainer.setRequestContext(null);
    super.afterCompletion(request, response, handler, ex);
  }

}
