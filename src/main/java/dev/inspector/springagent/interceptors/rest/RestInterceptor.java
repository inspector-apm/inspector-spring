package dev.inspector.springagent.interceptors.rest;

import dev.inspector.springagent.inspectors.RestInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestInterceptor implements HandlerInterceptor {

    @Autowired
    private RestInspector restInspector;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        restInspector.createTransaction("REST Transaction");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        restInspector.createSegment("REST async", "REST label");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        restInspector.flushTransaction("REST Context");
    }

}
