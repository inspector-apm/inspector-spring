package dev.inspector.springagent.interceptors.scheduler;

import dev.inspector.springagent.interceptors.InspectorBean;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SchedulerInterceptor {

    @Autowired
    private InspectorBean inspectorBean;

    @Before("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void beforeScheduledTask() {
        inspectorBean.createTransaction("Scheduler Transaction");
    }

    @After("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public void afterScheduledTask() {
        inspectorBean.createSegment("Scheduler async", "Scheduler label");
        inspectorBean.flushTransaction("Scheduler Context");
    }
}
