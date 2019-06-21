package com.soaringroad.blog.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class SrBlogContextContainer implements ApplicationContextAware {

    private static ApplicationContext context;
    private static ThreadLocal<RequestContext> requestContextThreadLocal = new ThreadLocal<RequestContext>();
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
    
    public RequestContext getRequestContext() {
      return requestContextThreadLocal.get();
    }
    
    public static void setRequestContext(RequestContext requestContext)  {
      requestContextThreadLocal.set(requestContext);
    }
    
    public static void setRequestId(String requestId) {
      RequestContext requestContext = requestContextThreadLocal.get();
      if (requestContext == null) {
        requestContext = new RequestContext();
        requestContextThreadLocal.set(requestContext);
      }
      requestContext.setRequestId(requestId);
    }
    
    public static String getRequestId() {
      return getRequestContextPropertyOptional(RequestContext::getRequestId);
    }
    
    
    public static void setSecurityKey(String securityKey) {
      RequestContext requestContext = requestContextThreadLocal.get();
      if (requestContext == null) {
        requestContext = new RequestContext();
        requestContextThreadLocal.set(requestContext);
      }
      requestContext.setSecurityKey(securityKey);
    }
    
    public static String getSecurityKey() {
      return getRequestContextPropertyOptional(RequestContext::getSecurityKey);
    }
    
    private static <T,U> U getRequestContextPropertyOptional(Function<RequestContext, U> mapper) {
      return Optional.ofNullable(requestContextThreadLocal.get()).map(mapper).orElse(null);
    }
    
    public static void clearRequestContext() {
      requestContextThreadLocal.remove();
    }

}
