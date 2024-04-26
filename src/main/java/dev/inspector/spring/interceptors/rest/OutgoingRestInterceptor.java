package dev.inspector.spring.interceptors.rest;

import dev.inspector.agent.executor.Inspector;
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
    Inspector inspector;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        System.out.println("Outgoing REST request intercepted.");
        inspector.startSegment("Outgoing REST async", "Outgoing REST label");
        return execution.execute(request, body);
    }
}
