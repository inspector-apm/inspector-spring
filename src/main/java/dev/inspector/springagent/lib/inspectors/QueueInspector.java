package dev.inspector.springagent.lib.inspectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueueInspector extends InspectorType {

    public QueueInspector(@Value("${inspector.ingestion-key}")String ingestionKey) {
        super(ingestionKey);
    }

}
