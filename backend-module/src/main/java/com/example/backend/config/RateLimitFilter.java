package com.example.backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter implements Filter {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = getClientIp(httpRequest);
        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, k -> new RequestCounter());

        if (counter.isRateLimited()) {
            httpResponse.setStatus(429);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"success\":false,\"message\":\"Rate limit excedido. Tente novamente em instantes.\"}");
            return;
        }

        counter.increment();
        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private static class RequestCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long windowStart = System.currentTimeMillis();

        boolean isRateLimited() {
            long now = System.currentTimeMillis();
            if (now - windowStart > 60_000) {
                count.set(0);
                windowStart = now;
            }
            return count.get() >= MAX_REQUESTS_PER_MINUTE;
        }

        void increment() {
            count.incrementAndGet();
        }
    }
}
