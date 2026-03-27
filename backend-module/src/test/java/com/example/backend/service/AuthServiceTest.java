package com.example.backend.service;

import com.example.backend.entity.UsuarioEntity;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private UsuarioEntity usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("$2a$10$encodedpassword");
        usuario.setAtivo(true);
        usuario.setRoles(Set.of("ROLE_USER"));
    }

    @Nested
    @DisplayName("Refresh Token Tests")
    class RefreshTokenTests {

        @Test
        @DisplayName("Should refresh token successfully")
        void shouldRefreshToken() {
            when(tokenProvider.validateToken("validRefreshToken")).thenReturn(true);
            when(tokenProvider.getUsernameFromToken("validRefreshToken")).thenReturn("testuser");
            when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuario));
            when(tokenProvider.generateAccessToken("testuser", Set.of("ROLE_USER"))).thenReturn("newAccessToken");

            String result = authService.refreshAccessToken("validRefreshToken");

            assertEquals("newAccessToken", result);
        }

        @Test
        @DisplayName("Should throw when refresh token is invalid")
        void shouldThrowWhenInvalidRefreshToken() {
            when(tokenProvider.validateToken("invalidToken")).thenReturn(false);

            assertThrows(BadCredentialsException.class, () -> authService.refreshAccessToken("invalidToken"));
        }
    }

    @Nested
    @DisplayName("Account Lockout Tests")
    class AccountLockoutTests {

        @Test
        @DisplayName("Should not be locked initially")
        void shouldNotBeLockedInitially() {
            assertFalse(authService.isAccountLocked("testuser"));
        }

        @Test
        @DisplayName("Should lock after 5 failed attempts")
        void shouldLockAfter5FailedAttempts() {
            for (int i = 0; i < 5; i++) {
                authService.recordLoginAttempt("testuser", false);
            }

            assertTrue(authService.isAccountLocked("testuser"));
        }

        @Test
        @DisplayName("Should reset on successful login")
        void shouldResetOnSuccess() {
            for (int i = 0; i < 3; i++) {
                authService.recordLoginAttempt("testuser", false);
            }
            authService.recordLoginAttempt("testuser", true);

            assertFalse(authService.isAccountLocked("testuser"));
        }
    }
}
