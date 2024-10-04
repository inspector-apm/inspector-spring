package dev.inspector.spring.interceptors.scheduler;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.TransactionType;
import dev.inspector.spring.interceptors.context.InspectorMonitoringContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
@Component
public class SchedulerInterceptor {

    @Autowired
    private InspectorMonitoringContext inspectorMonitoringContext;

    @Pointcut("@annotation(scheduled)")
    public void scheduledTask(Scheduled scheduled) {}

    @Before("@annotation(scheduled)")
    public void beforeScheduledTask(JoinPoint joinPoint, Scheduled scheduled) {
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        inspector.startTransaction(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()).withType(TransactionType.SCHEDULER);
    }

    @AfterThrowing(pointcut = "scheduledTask(scheduled)", throwing = "ex")
    public void handleScheduledTaskException(JoinPoint joinPoint, Scheduled scheduled, Throwable ex) {
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        inspector.reportException(ex);
        inspector.getTransaction().setResult("error");
    }

    @After("@annotation(scheduled)")
    public void afterScheduledTask(JoinPoint joinPoint, Scheduled scheduled) {
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        inspector.flush();
        inspectorMonitoringContext.removeInspectorService();
    }
}
