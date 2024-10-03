package dev.inspector.spring.utils;

public class StringUtils {

    public static String removePrefix(final String string, final String prefix) {
        if (!org.springframework.util.StringUtils.hasText(string)) {
            return "";
        }
        final int index = string.indexOf(prefix);
        if (index == 0) {
            return string.substring(prefix.length());
        } else {
            return string;
        }
    }
}
