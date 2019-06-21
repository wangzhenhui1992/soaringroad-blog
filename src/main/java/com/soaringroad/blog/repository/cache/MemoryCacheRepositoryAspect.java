/******************************************************************
 *   _____                  _             _____                 _  *
 *  / ____|                (_)           |  __ \               | | *
 * | (___   ___   __ _ _ __ _ _ __   __ _| |__) |___   __ _  __| | *
 *  \___ \ / _ \ / _` | '__| | '_ \ / _` |  _  // _ \ / _` |/ _` | *
 *  ____) | (_) | (_| | |  | | | | | (_| | | \ \ (_) | (_| | (_| | *
 * |_____/ \___/ \__,_|_|  |_|_| |_|\__, |_|  \_\___/ \__,_|\__,_| *
 *                                   __/ |                         *
 *                                  |___/                          *
 * Copyright ©2017-2020 www.soaringroad.com | All rights reserved. *
 ******************************************************************/
package com.soaringroad.blog.repository.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <pre>
 * 
 * </pre>
 */
@Slf4j
@Aspect
@Component
@ConditionalOnBean(MemoryCacheRepository.class)
public class MemoryCacheRepositoryAspect {


  @Pointcut("execution(* com.soaringroad.blog.repository.cache.MemoryCacheRepository.*(..))")
  public void pointcut() {}
  
  @Before("pointcut()")
  public void before(JoinPoint joinPoint) {
      Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
      List<String> argList = Arrays.asList(joinPoint.getArgs()).stream()
              .map(arg -> arg == null ? "null" : arg.toString()).collect(Collectors.toList());
      log.info("Calling MemoryCache function [" + method.getName() + "]  param=" + String.join(",", argList));
  }
  
  @AfterReturning(value = "pointcut()",returning = "value",argNames = "value")
  public void afterReturning(JoinPoint joinPoint, Object value) {
      Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
      log.info("Return MemoryCache result. ["+method.getName()+"]  value "+ (value==null? "not found" : "found"));
  }
}
