package dev.inspector.spring.interceptors.scheduler;

import dev.inspector.agent.executor.Inspector;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SchedulerInterceptor {

    @Autowired
    Inspector inspector;

    @Before("@annotation(scheduled)")
    public void beforeScheduledTask(JoinPoint joinPoint, Scheduled scheduled) {
        inspector.startTransaction(joinPoint.getSignature().getName());
    }

    @After("@annotation(scheduled)")
    public void afterScheduledTask(JoinPoint joinPoint, Scheduled scheduled) {
        inspector.flush();
    }
}
