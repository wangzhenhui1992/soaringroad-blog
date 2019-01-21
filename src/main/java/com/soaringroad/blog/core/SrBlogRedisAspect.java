package com.soaringroad.blog.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class SrBlogRedisAspect {
    
    @Pointcut("execution(* com.soaringroad.blog.repository.RedisRepository.*(..))")
    public void pointcut() {}
    
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        List<String> argList = Arrays.asList(joinPoint.getArgs()).stream()
                .map(arg -> arg == null ? "null" : arg.toString()).collect(Collectors.toList());
        log.info("Calling Redis function [" + method.getName() + "]  param=" + String.join(",", argList));
    }
    
    @AfterReturning(value = "pointcut()",returning = "value",argNames = "value")
    public void afterReturning(JoinPoint joinPoint, Object value) {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        log.info("Return Redis result. ["+method.getName()+"]  value "+ (value==null? "not found" : "found"));
    }
    
    
}
