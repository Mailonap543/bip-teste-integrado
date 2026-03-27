package com.example.backend.service;

import com.example.backend.entity.UsuarioEntity;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MS = 15 * 60 * 1000;

    private final Map<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public String refreshAccessToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Refresh token inválido ou expirado");
        }
        String username = tokenProvider.getUsernameFromToken(refreshToken);
        UsuarioEntity usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        Set<String> roles = usuario.getRoles();
        return tokenProvider.generateAccessToken(username, roles);
    }

    public void recordLoginAttempt(String username, boolean success) {
        LoginAttempt attempt = loginAttempts.computeIfAbsent(username, k -> new LoginAttempt());
        if (success) {
            attempt.reset();
        } else {
            attempt.increment();
            log.warn("Failed login attempt for user '{}': {}/{}", username, attempt.getCount(), MAX_LOGIN_ATTEMPTS);
        }
    }

    public boolean isAccountLocked(String username) {
        LoginAttempt attempt = loginAttempts.get(username);
        if (attempt == null) return false;
        if (attempt.getCount() >= MAX_LOGIN_ATTEMPTS) {
            if (System.currentTimeMillis() - attempt.getLastAttempt() < LOCKOUT_DURATION_MS) {
                return true;
            }
            attempt.reset();
        }
        return false;
    }

    private static class LoginAttempt {
        private int count = 0;
        private long lastAttempt = 0;

        void increment() {
            count++;
            lastAttempt = System.currentTimeMillis();
        }

        void reset() {
            count = 0;
            lastAttempt = 0;
        }

        int getCount() { return count; }
        long getLastAttempt() { return lastAttempt; }
    }
}
