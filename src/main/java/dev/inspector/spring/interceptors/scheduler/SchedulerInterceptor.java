package dev.inspector.spring.interceptors.scheduler;

import dev.inspector.agent.executor.Inspector;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SchedulerInterceptor {

    @Autowired
    Inspector inspector;

    @Before("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void beforeScheduledTask() {
        System.out.println("Scheduler intercepted.");
        inspector.startTransaction("Scheduler Transaction");
    }

    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void afterScheduledTask() {
        inspector.flush();
    }
}
