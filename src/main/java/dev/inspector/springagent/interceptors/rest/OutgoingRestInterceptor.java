package dev.inspector.springagent.interceptors.rest;

import dev.inspector.springagent.inspectors.InspectorPicker;
import dev.inspector.springagent.inspectors.InspectorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OutgoingRestInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private InspectorPicker inspectorPicker;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        InspectorType currentInspector = inspectorPicker.getCurrentInspector();
        if (currentInspector != null)
            currentInspector.createSegment("Outgoing REST async", "Outgoing REST label");
        // Returning response
        return execution.execute(request, body);
    }
}
