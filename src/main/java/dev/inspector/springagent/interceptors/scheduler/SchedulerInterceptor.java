package dev.inspector.springagent.interceptors.scheduler;

import dev.inspector.springagent.inspectors.SchedulerInspector;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SchedulerInterceptor {

    @Autowired
    private SchedulerInspector schedulerInspector;

    @Before("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void beforeScheduledTask() {
        System.out.println("Scheduler intercepted.");
        schedulerInspector.createTransaction("Scheduler Transaction");
    }

    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void afterScheduledTask() {
        schedulerInspector.closeTransaction("Scheduler Context");
    }
}
