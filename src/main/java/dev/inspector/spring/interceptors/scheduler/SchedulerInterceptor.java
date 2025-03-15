package dev.inspector.spring.interceptors.scheduler;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.TransactionType;
import dev.inspector.spring.interceptors.context.InspectorMonitoringContext;
import dev.inspector.spring.interceptors.rest.RestInterceptor;
import dev.inspector.spring.utils.BlacklistMatcher;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.AfterThrowing;

@Aspect
@Component
public class SchedulerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerInterceptor.class);

    @Autowired
    private InspectorMonitoringContext inspectorMonitoringContext;

    @Autowired
    private BlacklistMatcher blacklistMatcher;

    @Pointcut("@annotation(scheduled)")
    public void scheduledTask(Scheduled scheduled) {}

    @Before("@annotation(scheduled)")
    public void beforeScheduledTask(JoinPoint joinPoint, Scheduled scheduled) {
        String taskName = deriveTaskName(joinPoint);
        if (blacklistMatcher.isTaskBlacklisted(taskName)) {
            LOGGER.debug(
                    "Blacklisted scheduled task. Skipping monitoring. Task name {}",
                    taskName
            );
        } else {
            Inspector inspector = inspectorMonitoringContext.getInspectorService();
            inspector.startTransaction(taskName).withType(TransactionType.SCHEDULER);
        }

    }

    @AfterThrowing(pointcut = "scheduledTask(scheduled)", throwing = "ex")
    public void handleScheduledTaskException(JoinPoint joinPoint, Scheduled scheduled, Throwable ex) {
        String taskName = deriveTaskName(joinPoint);

        if (!blacklistMatcher.isTaskBlacklisted(taskName)) {
            Inspector inspector = inspectorMonitoringContext.getInspectorService();
            inspector.reportException(ex);
            inspector.getTransaction().setResult("error");
        }
    }

    @After("@annotation(scheduled)")
    public void afterScheduledTask(JoinPoint joinPoint, Scheduled scheduled) {
        String taskName = deriveTaskName(joinPoint);

        if (!blacklistMatcher.isTaskBlacklisted(taskName)) {
            Inspector inspector = inspectorMonitoringContext.getInspectorService();
            inspector.flush();
            inspectorMonitoringContext.removeInspectorService();
        }
    }

    private String deriveTaskName(JoinPoint scheduledTaskJointPoint) {
        return scheduledTaskJointPoint.getSignature().getDeclaringTypeName() + "." + scheduledTaskJointPoint.getSignature().getName();
    }
}
