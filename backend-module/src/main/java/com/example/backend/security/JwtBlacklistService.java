package com.example.backend.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlacklistService {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    public void blacklist(String token, long expirationMs) {
        blacklistedTokens.put(token, System.currentTimeMillis() + expirationMs);
        cleanup();
    }

    public boolean isBlacklisted(String token) {
        Long expiry = blacklistedTokens.get(token);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            blacklistedTokens.remove(token);
            return false;
        }
        return true;
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
    }
}
