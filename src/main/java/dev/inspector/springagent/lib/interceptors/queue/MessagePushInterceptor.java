package dev.inspector.springagent.lib.interceptors.queue;

import javax.jms.JMSException;
import javax.jms.Message;

import dev.inspector.springagent.lib.inspectors.InspectorPicker;
import dev.inspector.springagent.lib.inspectors.InspectorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MessagePushInterceptor implements MessagePostProcessor {

    @Autowired
    private InspectorPicker inspectorPicker;

    @Override
    public Message postProcessMessage(Message message) {
        System.out.println("Message push intercepted.");
        InspectorType currentInspector = inspectorPicker.getCurrentInspector();
        if (currentInspector != null)
            currentInspector.createSegment("Message push async", "Message push label");
        return message;
    }
}
