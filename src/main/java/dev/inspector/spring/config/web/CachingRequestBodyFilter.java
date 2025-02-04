package dev.inspector.spring.config.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;

@Component
public class CachingRequestBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest requestWithCachedBody = new ContentCachingRequestWrapper((HttpServletRequest) request);

            chain.doFilter(requestWithCachedBody, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
