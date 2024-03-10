package dev.inspector.springagent.lib.inspectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QueueInspector extends AbstractInspector {

    public QueueInspector(@Value("${inspector.ingestion-key}")String ingestionKey, @Value("${inspector.ingestion-url}")String ingestionUrl) {
        super(ingestionKey, ingestionUrl);
    }

}
