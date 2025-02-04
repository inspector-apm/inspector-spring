package dev.inspector.spring.interceptors.rest;

import dev.inspector.agent.executor.Inspector;
import dev.inspector.agent.model.Transaction;
import dev.inspector.agent.model.TransactionType;
import dev.inspector.spring.interceptors.context.InspectorMonitoringContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class RestInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestInterceptor.class);

    @Autowired
    private InspectorMonitoringContext inspectorMonitoringContext;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        Transaction transaction = inspector.startTransaction(String.format("%s %s", request.getMethod(), pattern)).withType(TransactionType.REQUEST);

        LOGGER.debug(
                "Thread {}: Incoming http request intercepted. Starting monitoring transaction with hash {} ",
                Thread.currentThread().getName(),
                transaction.getBasicTransactionInfo().getHash()
        );

        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        Inspector inspector = inspectorMonitoringContext.getInspectorService();
        Transaction transaction = inspector.getTransaction();
        transaction.setResult(String.valueOf(response.getStatus()));

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("method", request.getMethod());
        jsonRequest.put("version", request.getProtocol());

        JSONObject socketDetails = new JSONObject();
        socketDetails.put("remote_address", request.getRemoteAddr());
        socketDetails.put("encrypted", request.isSecure());
        jsonRequest.put("socket", socketDetails);

        JSONObject cookies = new JSONObject();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookies.put(cookie.getName(), cookie.getValue());
            }
        }
        jsonRequest.put("cookies", cookies);

        JSONObject headers = new JSONObject();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        jsonRequest.put("headers", headers);

        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) request;

            JSONArray fileNamesArray = new JSONArray();
            multipartRequest.getFileNames().forEachRemaining(fileNamesArray::put);

            JSONArray formDataArray = new JSONArray();
            multipartRequest.getParameterMap().forEach((key, value) -> {
                JSONObject formDataObj = new JSONObject();
                formDataObj.put(key, value);

                formDataArray.put(formDataObj);
            });

            JSONObject multipartPayload = new JSONObject();
            multipartPayload.put("files", fileNamesArray);
            multipartPayload.put("data", formDataArray);

            jsonRequest.put("body", multipartPayload);
        } else {

            ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
            String body = new String(cachedRequest.getContentAsByteArray(), request.getCharacterEncoding());
            jsonRequest.put("body", body);
        }

        transaction.addContext("Request", jsonRequest);

        JSONObject jsonUrl = new JSONObject();
        jsonUrl.put("protocol", request.getScheme());
        jsonUrl.put("port", request.getServerPort());
        jsonUrl.put("path", request.getRequestURI());
        jsonUrl.put("search", request.getQueryString());
        jsonUrl.put("full", request.getRequestURL().toString() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));

        transaction.addContext("URL", jsonUrl);

        LOGGER.debug(
                "Thread {}: Incoming http request response interceptor. Flushing monitoring transaction with hash {} ",
                Thread.currentThread().getName(),
                transaction.getBasicTransactionInfo().getHash()
        );

        inspector.flush();
        inspectorMonitoringContext.removeInspectorService();
    }
}
