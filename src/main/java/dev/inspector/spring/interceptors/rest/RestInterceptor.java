package dev.inspector.spring.interceptors.rest;

import dev.inspector.spring.config.ConfigProperties;
import dev.inspector.spring.inspectors.RestInspector;
import dev.inspector.agent.executor.Inspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RestInterceptor implements HandlerInterceptor {

    @Autowired
    Inspector inspector;

    @Autowired
    private ConfigProperties configProperties;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Extract the original URI template
        String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        // VERIFICO SE IL PATH E' PRESENTE TRA QUELLI PASSATI DALLO YAML DEL PROGETTO CHE IMPLEMENTA LA LIBRERIA
        if (pattern != null & configProperties.getPathList().contains(pattern)) {
            // Use the pattern as needed, for example, logging or transaction naming

            // STAMPO TUTTI I PATH CONFIGURATI NELLO YAML DEL PROGETTO CHE IMPLEMENTA LA LIBRERIA
            configProperties.getPathList().forEach(x -> System.out.println(x));

            inspector.startTransaction(String.format("%s - %s", request.getMethod(), pattern));
        } else {
            System.out.println("Incoming REST request intercepted.");
            inspector.startTransaction("%s - %s", request.getMethod(), pattern));
        }

        return true;
    }


    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        inspector.closeTransaction("REST Context");
    }

}
