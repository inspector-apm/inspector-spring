package dev.inspector.spring.interceptors.context;


import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MonitoringContextHolder {

    private Config inspectorConfig;

    private ThreadLocal<Inspector> inspectorServiceThreadSafeWrapper;

    public MonitoringContextHolder(@Autowired Config inspectorConfig) {
        this.inspectorConfig = inspectorConfig;
        inspectorServiceThreadSafeWrapper = new ThreadLocal<>();
    }

    public Inspector getInspectorService() {
        Inspector inspectorService = inspectorServiceThreadSafeWrapper.get();
        if (inspectorService == null) {
            inspectorService = new Inspector(inspectorConfig);
            inspectorServiceThreadSafeWrapper.set(inspectorService);
        }

        return inspectorService;
    }

    public void removeInspectorService() {
        inspectorServiceThreadSafeWrapper.remove();
    }

}
