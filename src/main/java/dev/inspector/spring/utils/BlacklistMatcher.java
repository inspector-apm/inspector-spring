package dev.inspector.spring.utils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class BlacklistMatcher {

    @Value("${inspector.url-paths-blacklist:}")
    private List<String> urlsPathsBlacklist;

    @Value("${inspector.tasks-blacklist:}")
    private List<String> scheduledTasksBlacklist;

    private List<Pattern> urlBlacklistPatterns;

    private List<Pattern> tasksBlacklistPatterns;

    @PostConstruct
    private void init() {
        this.urlBlacklistPatterns = urlsPathsBlacklist.stream()
                .map(this::createWildcardPattern)
                .collect(Collectors.toList());

        this.tasksBlacklistPatterns = scheduledTasksBlacklist.stream()
                .map(this::createWildcardPattern)
                .collect(Collectors.toList());
    }

    private Pattern createWildcardPattern(String pattern) {
        // Convert custom wildcard pattern to regex
        String regex = pattern
                .replace(".", "\\.")
                .replace("*", ".*")     // Convert * to .*
                .replace("?", "\\?")    // Escape other special chars
                .replace("+", "\\+")
                .replace("$", "\\$")
                .replace("^", "\\^");

        return Pattern.compile("^" + regex + "$");
    }

    public boolean isRequestBlacklisted(HttpServletRequest request) {
        String path = request.getRequestURI();

        for (Pattern pattern : urlBlacklistPatterns) {
            if (pattern.matcher(path).matches()) {
                return true;
            }
        }
        return false;
    }

    public boolean isTaskBlacklisted(String taskName) {
        for (Pattern pattern : tasksBlacklistPatterns) {
            if (pattern.matcher(taskName).matches()) {
                return true;
            }
        }
        return false;
    }
}