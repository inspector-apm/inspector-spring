package dev.inspector.springagent.inspectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InspectorPicker {

    @Autowired
    private RestInspector restInspector;
    @Autowired
    private SchedulerInspector schedulerInspector;

    public InspectorType getCurrentInspector() {
        long currentThreadId = Thread.currentThread().getId();
        InspectorType currentInspector = null;
        if (currentThreadId == restInspector.getThreadId())
            currentInspector = restInspector;
        else if (currentThreadId == schedulerInspector.getThreadId())
            currentInspector = schedulerInspector;
        return currentInspector;
    }
}
