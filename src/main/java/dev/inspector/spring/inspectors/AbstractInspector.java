package dev.inspector.spring.inspectors;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Config;
import dev.inspector.agent.model.Segment;
import dev.inspector.agent.model.Transaction;
import dev.inspector.agent.utility.JsonBuilder;

import static dev.inspector.agent.App.waitMillis;

public abstract class AbstractInspector {

    private final Inspector inspector;
    private Transaction transaction;
    private long threadId;

    public AbstractInspector(String ingestionKey, String timeToFlush) {
        Config config = new Config(ingestionKey, timeToFlush);
        Inspector inspector = new Inspector(config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> onShutdown(inspector)));
        this.inspector = inspector;
    }

    private void onShutdown(Inspector inspector) {
        System.out.println("qui c'è il flush finale");
        inspector.shutdown();
    }

    public void createSegment(String segmentType, String segmentLabel) {
        Segment segmentRef = this.inspector.addSegment((segment) -> {
            waitMillis(1000L);
            String ptr = null;
            if (((String) ptr).equals("exception")) {
                System.out.println(1234);
            }

            return segment;
        }, segmentType, segmentLabel, false);
        segmentRef.addContext("view1", (new JsonBuilder()).put("test", "test2").build());
    }

    public void createTransaction(String transactionName) {
        this.threadId = Thread.currentThread().getId();
        this.transaction = this.inspector.startTransaction(transactionName);
    }

    public void closeTransaction(String contextLabel) {
        this.transaction.setResult("SUCCESS");
        this.transaction.addContext(contextLabel, (new JsonBuilder()).put("contextkey", "contextvalue").build());
        this.transaction.end();
    }

    public Inspector getInspector() {
        return this.inspector;
    }

    public long getThreadId() {
        return this.threadId;
    }
}
