package com.example.backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Pattern;

@Component
@Order(1)
public class InputSanitizationFilter implements Filter {

    private static final Pattern XSS_PATTERN = Pattern.compile(
            "<script.*?>.*?</script>|javascript:|on\\w+\\s*=",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new SanitizedRequest((HttpServletRequest) request), response);
    }

    private static class SanitizedRequest extends HttpServletRequestWrapper {

        SanitizedRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return sanitize(value);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) return null;
            String[] sanitized = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                sanitized[i] = sanitize(values[i]);
            }
            return sanitized;
        }

        private String sanitize(String value) {
            if (value == null) return null;
            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            value = XSS_PATTERN.matcher(value).replaceAll("");
            return value.trim();
        }
    }
}
