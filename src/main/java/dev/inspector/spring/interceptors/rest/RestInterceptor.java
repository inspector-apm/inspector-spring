package dev.inspector.spring.interceptors.rest;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public class RestInterceptor implements HandlerInterceptor {

    @Autowired
    Inspector inspector;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        Transaction transaction = inspector.startTransaction(String.format("%s %s", request.getMethod(), pattern));

        // Crea l'oggetto JSON per la request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("method", request.getMethod());
        jsonRequest.put("version", request.getProtocol());

        // Aggiungi i dettagli del socket
        JSONObject socketDetails = new JSONObject();
        socketDetails.put("remote_address", request.getRemoteAddr());
        socketDetails.put("encrypted", request.isSecure());
        jsonRequest.put("socket", socketDetails);

        // Aggiungi i cookies
        JSONObject cookies = new JSONObject();
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        jsonRequest.put("cookies", cookies);

        // Aggiungi gli headers
        JSONObject headers = new JSONObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        jsonRequest.put("headers", headers);

        // Leggi il body della richiesta
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        jsonRequest.put("body", body);

        // Aggiungi il contesto della request alla transazione
        transaction.addContext("Request", jsonRequest);

        // Crea l'oggetto JSON per l'URL
        JSONObject jsonUrl = new JSONObject();
        jsonUrl.put("protocol", request.getScheme());
        jsonUrl.put("port", request.getServerPort());
        jsonUrl.put("path", request.getRequestURI());
        jsonUrl.put("search", request.getQueryString());
        jsonUrl.put("full", request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));

        // Aggiungi il contesto dell'URL alla transazione
        transaction.addContext("URL", jsonUrl);

        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Transaction transaction = inspector.getTransaction();
        transaction.setResult(String.valueOf(response.getStatus()));
        inspector.flush();
    }
}
