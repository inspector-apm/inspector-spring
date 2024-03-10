package dev.inspector.springagent.lib.interceptors.queue;

import javax.jms.Message;

import dev.inspector.springagent.lib.inspectors.CurrentInspectorResolver;
import dev.inspector.springagent.lib.inspectors.AbstractInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MessagePushInterceptor implements MessagePostProcessor {

    @Autowired
    private CurrentInspectorResolver currentInspectorResolver;

    @Override
    public Message postProcessMessage(Message message) {
        System.out.println("Message push intercepted.");
        AbstractInspector currentInspector = currentInspectorResolver.getCurrentInspector();
        if (currentInspector != null)
            currentInspector.createSegment("Message push async", "Message push label");
        return message;
    }
}
