//package dev.inspector.springagent.app.queue;
//
//
//import dev.inspector.springagent.lib.inspectors.CurrentInspectorResolver;
//import dev.inspector.springagent.lib.inspectors.AbstractInspector;
//import dev.inspector.springagent.lib.inspectors.QueueInspector;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class QueueInterceptor {
//
//    @Autowired
//    CurrentInspectorResolver currentInspectorResolver;
//    @Autowired
//    QueueInspector queueInspector;
//
//    @JmsListener(destination = "*")
//    public void onMessage(String message) {
//        System.out.println("Message on queue intercepted: " + message);
//        AbstractInspector currentInspector = currentInspectorResolver.getCurrentInspector();
//        if (currentInspector == null) {
//            queueInspector.createTransaction("Queue Transaction");
//            queueInspector.createSegment("Queue async", "Queue label");
//            queueInspector.closeTransaction("Queue Context");
//        } else {
//            currentInspector.createSegment("Queue async", "Queue label");
//        }
//    }
//
//}
