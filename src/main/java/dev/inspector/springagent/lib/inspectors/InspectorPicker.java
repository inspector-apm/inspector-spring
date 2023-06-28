package dev.inspector.springagent.lib.inspectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InspectorPicker {

    @Autowired
    private RestInspector restInspector;
    @Autowired
    private SchedulerInspector schedulerInspector;
    @Autowired
    private QueueInspector queueInspector;

    public InspectorType getCurrentInspector() {
        long currentThreadId = Thread.currentThread().getId();
        InspectorType currentInspector = null;
        if (currentThreadId == restInspector.getThreadId())
            currentInspector = restInspector;
        else if (currentThreadId == schedulerInspector.getThreadId())
            currentInspector = schedulerInspector;
        else if (currentThreadId == queueInspector.getThreadId())
            currentInspector = queueInspector;
        return currentInspector;
    }
}
