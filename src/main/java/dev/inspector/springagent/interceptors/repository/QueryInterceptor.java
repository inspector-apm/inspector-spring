package dev.inspector.springagent.interceptors.repository;

import dev.inspector.springagent.inspectors.InspectorPicker;
import dev.inspector.springagent.inspectors.InspectorType;
import dev.inspector.springagent.inspectors.RestInspector;
import dev.inspector.springagent.inspectors.SchedulerInspector;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QueryInterceptor {

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
