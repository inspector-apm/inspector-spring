package dev.inspector.springagent.lib.interceptors.rest;

import dev.inspector.springagent.lib.inspectors.RestInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestInterceptor implements HandlerInterceptor {

    @Autowired
    private RestInspector restInspector;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Extract the original URI template
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        if (pattern != null) {
            System.out.println("Original endpoint pattern: " + pattern);
            // Use the pattern as needed, for example, logging or transaction naming
            restInspector.createTransaction("REST Transaction: " + pattern);
        } else {
            System.out.println("Incoming REST request intercepted.");
            restInspector.createTransaction("REST Transaction");
        }

        return true;
    }

//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        restInspector.createSegment("REST async", "REST label");
//    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        restInspector.closeTransaction("REST Context");
    }

}
