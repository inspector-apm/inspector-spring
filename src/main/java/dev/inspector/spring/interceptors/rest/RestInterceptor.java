package dev.inspector.spring.interceptors.rest;

import dev.inspector.agent.executor.Inspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestInterceptor implements HandlerInterceptor {

    @Autowired
    Inspector inspector;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        inspector.startTransaction(String.format("%s - %s", request.getMethod(), pattern));
        //TODO: Add headers to the transaction usando il context. aggiungere nella mappa context:
        // - request e come value ci metto tutto, dall'header alla query string
        // - url

        return true;
    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        //TODO: riprendere l'oggetto inspector e aggiungere context "label response con la response"
        inspector.flush();
    }

}
