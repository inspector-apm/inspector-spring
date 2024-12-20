package dev.inspector.spring.interceptors.rest;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Segment;
import dev.inspector.spring.interceptors.context.InspectorMonitoringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestTemplateMonitoringInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateMonitoringInterceptor.class);

    @Autowired
    private InspectorMonitoringContext inspectorMonitoringContext;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        LOGGER.debug(
                "Thread {}: Outgoing HTTP call from RestTemplate client intercepted. Starting monitoring segment",
                Thread.currentThread().getName()
        );
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        Segment segment = inspector.startSegment("http", request.getURI().toString());
        try {
            return execution.execute(request, body);
        } finally {
            segment.end();
        }
    }
}
