package dev.inspector.springagent.lib.interceptors.scheduler;

import dev.inspector.springagent.lib.inspectors.SchedulerInspector;
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
        schedulerInspector.createTransaction("Scheduler Transaction");
    }

    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void afterScheduledTask() {
        schedulerInspector.createSegment("Scheduler async", "Scheduler label");
        schedulerInspector.closeTransaction("Scheduler Context");
    }
}
