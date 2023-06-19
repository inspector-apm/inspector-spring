package dev.inspector.springagent.inspectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SchedulerInspector extends InspectorType {

    public SchedulerInspector(@Value("${inspector.ingestion-key}")String ingestionKey) {
        super(ingestionKey);
    }

}
