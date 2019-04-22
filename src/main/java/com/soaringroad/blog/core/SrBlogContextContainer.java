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
    private static ThreadLocal<RequestContext> requestContextThreadLocal = null;
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
      return Optional.ofNullable(requestContextThreadLocal).map(ThreadLocal<RequestContext>::get).orElse(null);
    }
    
    public static void setRequestContext(RequestContext requestContext)  {
      initIfNotExist(false);
      requestContextThreadLocal.set(requestContext);
    }
    
    public static void setRequestId(String requestId) {
      initIfNotExist(true);
      requestContextThreadLocal.get().setRequestId(requestId);
    }
    
    public static String getRequestId() {
      return getRequestContextPropertyOptional(RequestContext::getRequestId);
    }
    
    
    public static void setSecurityKey(String securityKey) {
      initIfNotExist(true);
      requestContextThreadLocal.get().setSecurityKey(securityKey);
    }
    
    public static String getSecurityKey() {
      return getRequestContextPropertyOptional(RequestContext::getSecurityKey);
    }
    
    private static void initIfNotExist(boolean initContext) {
      if (requestContextThreadLocal != null) {
        return;
      }
      requestContextThreadLocal = new  ThreadLocal<RequestContext>();
      if (!initContext) {
        return;
      }
      requestContextThreadLocal.set(new RequestContext());
    }
    
    private static <T,U> U getRequestContextPropertyOptional(Function<RequestContext, U> mapper) {
      return Optional.ofNullable(requestContextThreadLocal).map(ThreadLocal<RequestContext>::get).map(mapper).orElse(null);
    }

}
