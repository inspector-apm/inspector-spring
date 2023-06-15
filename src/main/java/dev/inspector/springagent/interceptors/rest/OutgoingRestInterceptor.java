package dev.inspector.springagent.interceptors.rest;

import dev.inspector.springagent.interceptors.InspectorBean;
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
    private InspectorBean inspectorBean;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        inspectorBean.createSegment("Outgoing REST async", "Outgoing REST label");
        // Returning response
        return execution.execute(request, body);
    }
}
