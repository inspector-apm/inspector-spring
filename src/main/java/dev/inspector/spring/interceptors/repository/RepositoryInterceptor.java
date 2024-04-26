package dev.inspector.spring.interceptors.repository;

import dev.inspector.agent.executor.Inspector;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryInterceptor {

    @Autowired
    Inspector inspector;

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object interceptQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        inspector.startSegment("Query async", "Query label");
        return joinPoint.proceed();
    }



}
