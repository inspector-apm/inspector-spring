package dev.inspector.springagent.inspectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentInspectorResolver {

    @Autowired
    private RestInspector restInspector;
    @Autowired
    private SchedulerInspector schedulerInspector;
    @Autowired
    private QueueInspector queueInspector;

    public AbstractInspector getCurrentInspector() {
        Long currentThreadId = Thread.currentThread().getId();
        AbstractInspector currentInspector = null;
        if (currentThreadId == restInspector.getThreadId())
            currentInspector = restInspector;
        else if (currentThreadId == schedulerInspector.getThreadId())
            currentInspector = schedulerInspector;
        else if (currentThreadId == queueInspector.getThreadId())
            currentInspector = queueInspector;
        return currentInspector;
    }
}
