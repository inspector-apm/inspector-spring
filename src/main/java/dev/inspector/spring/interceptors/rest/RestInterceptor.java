package dev.inspector.spring.interceptors.rest;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Transaction;
import dev.inspector.spring.utils.http.request.CachedBodyHttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Component
public class RestInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestInterceptor.class);

    @Autowired
    Inspector inspector;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        CachedBodyHttpServletRequest cachedHttpRequest = new CachedBodyHttpServletRequest(request);
        String pattern = (String) cachedHttpRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        Transaction transaction = inspector.startTransaction(String.format("%s %s", cachedHttpRequest.getMethod(), pattern));

        LOGGER.debug(
                "Thread {}: Incoming http request intercepted. Starting monitoring transaction with hash {} ",
                Thread.currentThread().getName(),
                transaction.getBasicTransactionInfo().getHash()
        );

        // Crea l'oggetto JSON per la request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("method", cachedHttpRequest.getMethod());
        jsonRequest.put("version", cachedHttpRequest.getProtocol());

        // Aggiungi i dettagli del socket
        JSONObject socketDetails = new JSONObject();
        socketDetails.put("remote_address", cachedHttpRequest.getRemoteAddr());
        socketDetails.put("encrypted", cachedHttpRequest.isSecure());
        jsonRequest.put("socket", socketDetails);

        // Aggiungi i cookies
        JSONObject cookies = new JSONObject();
        if (cachedHttpRequest.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : cachedHttpRequest.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        jsonRequest.put("cookies", cookies);

        // Aggiungi gli headers
        JSONObject headers = new JSONObject();
        Enumeration<String> headerNames = cachedHttpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, cachedHttpRequest.getHeader(headerName));
        }
        jsonRequest.put("headers", headers);

        // Leggi il body della richiesta
        String body = cachedHttpRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        jsonRequest.put("body", body);

        // Aggiungi il contesto della request alla transazione
        transaction.addContext("Request", jsonRequest);

        // Crea l'oggetto JSON per l'URL
        JSONObject jsonUrl = new JSONObject();
        jsonUrl.put("protocol", cachedHttpRequest.getScheme());
        jsonUrl.put("port", cachedHttpRequest.getServerPort());
        jsonUrl.put("path", cachedHttpRequest.getRequestURI());
        jsonUrl.put("search", cachedHttpRequest.getQueryString());
        jsonUrl.put("full", cachedHttpRequest.getRequestURL().toString() + (cachedHttpRequest.getQueryString() != null ? "?" + cachedHttpRequest.getQueryString() : ""));

        // Aggiungi il contesto dell'URL alla transazione
        transaction.addContext("URL", jsonUrl);

        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Transaction transaction = inspector.getTransaction();
        transaction.setResult(String.valueOf(response.getStatus()));
        LOGGER.debug(
                "Thread {}: Incoming http request response interceptor. Flushing monitoring transaction with hash {} ",
                Thread.currentThread().getName(),
                transaction.getBasicTransactionInfo().getHash()
        );

        inspector.flush();
    }
}
