package dev.inspector.spring.interceptors.rest;

import dev.inspector.spring.inspectors.CurrentInspectorResolver;
import dev.inspector.spring.inspectors.AbstractInspector;
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
    private CurrentInspectorResolver currentInspectorResolver;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Outgoing REST request intercepted.");
        AbstractInspector currentInspector = currentInspectorResolver.getCurrentInspector();
        if (currentInspector != null)
            currentInspector.createSegment("Outgoing REST async", "Outgoing REST label");
        // Returning response
        return execution.execute(request, body);
    }
}
