package dev.inspector.springagent.lib.interceptors.repository;

import dev.inspector.springagent.lib.inspectors.InspectorPicker;
import dev.inspector.springagent.lib.inspectors.InspectorType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryInterceptor {

    @Autowired
    InspectorPicker inspectorPicker;

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object interceptQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        InspectorType currentInspector = inspectorPicker.getCurrentInspector();
        if (currentInspector != null)
            currentInspector.createSegment("Query async", "Query label");
        return joinPoint.proceed();
    }



}
