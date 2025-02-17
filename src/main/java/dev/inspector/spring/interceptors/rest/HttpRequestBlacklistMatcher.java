package dev.inspector.spring.interceptors.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Component
public class HttpRequestBlacklistMatcher {

    @Value("${inspector.url-paths-blacklist:}")
    private List<String> urlsPathsBlacklist;

    public boolean isRequestBlacklisted(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestPath = request.getRequestURI();

        for (String pattern : urlsPathsBlacklist) {
            if (antPathMatcher.match(pattern, requestPath)) {
                return true;
            }
        }
        return false;
    }

}
