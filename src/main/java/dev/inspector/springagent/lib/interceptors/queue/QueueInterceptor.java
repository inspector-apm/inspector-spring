package dev.inspector.springagent.lib.interceptors.queue;

import dev.inspector.springagent.lib.inspectors.InspectorPicker;
import dev.inspector.springagent.lib.inspectors.InspectorType;
import dev.inspector.springagent.lib.inspectors.QueueInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueInterceptor {

    @Autowired
    InspectorPicker inspectorPicker;
    @Autowired
    QueueInspector queueInspector;

    @JmsListener(destination = "*")
    public void onMessage(String message) {
        System.out.println("Message intercepted: " + message);
        InspectorType currentInspector = inspectorPicker.getCurrentInspector();
        if (currentInspector == null) {
            queueInspector.createTransaction("Queue Transaction");
            queueInspector.createSegment("Queue async", "Queue label");
            queueInspector.closeTransaction("Queue Context");
        } else {
            currentInspector.createSegment("Queue async", "Queue label");
        }
    }

}
