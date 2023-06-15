package dev.inspector.springagent.interceptors.repository;

import dev.inspector.springagent.interceptors.InspectorBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QueryInterceptor {

    @Autowired
    private InspectorBean inspectorBean;

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object interceptQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        inspectorBean.createSegment("Query async", "Query label");
        return joinPoint.proceed();
    }

}
