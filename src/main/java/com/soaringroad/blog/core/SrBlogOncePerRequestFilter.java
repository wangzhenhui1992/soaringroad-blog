package com.soaringroad.blog.core;

import com.alibaba.druid.util.StringUtils;
import com.soaringroad.blog.util.SrBlogConsts;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SrBlogOncePerRequestFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String requestId = request.getHeader(SrBlogConsts.REQUEST_ID);
    if (StringUtils.isEmpty(requestId)) {
      requestId = UUID.randomUUID().toString();
    }
    MDC.put(SrBlogConsts.REQUEST_ID, requestId);
    SrBlogContextContainer.setRequestId(requestId);
    SrBlogContextContainer.setSecurityKey(request.getHeader(HttpHeaders.AUTHORIZATION));
    filterChain.doFilter(request, response);
  }

}
