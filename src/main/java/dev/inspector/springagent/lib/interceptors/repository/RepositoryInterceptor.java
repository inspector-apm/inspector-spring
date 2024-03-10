package dev.inspector.springagent.lib.interceptors.repository;

import dev.inspector.springagent.lib.inspectors.CurrentInspectorResolver;
import dev.inspector.springagent.lib.inspectors.AbstractInspector;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryInterceptor {

    @Autowired
    CurrentInspectorResolver currentInspectorResolver;

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object interceptQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        AbstractInspector currentInspector = currentInspectorResolver.getCurrentInspector();
        if (currentInspector != null)
            currentInspector.createSegment("Query async", "Query label");
        return joinPoint.proceed();
    }



}
