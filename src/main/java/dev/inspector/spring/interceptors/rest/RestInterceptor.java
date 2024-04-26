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
        // Extract the original URI template
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        if (pattern != null) {
            // Use the pattern as needed, for example, logging or transaction naming
            inspector.startTransaction("REST Transaction: " + pattern);
        } else {
            System.out.println("Incoming REST request intercepted.");
            inspector.startTransaction("REST Transaction");
        }

        return true;
    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        inspector.closeTransaction("REST Context");
    }

}
