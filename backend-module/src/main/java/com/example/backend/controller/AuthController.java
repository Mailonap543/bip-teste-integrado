package com.example.backend.controller;

import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RefreshTokenRequestDTO;
import com.example.backend.entity.UsuarioEntity;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação JWT")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Realiza login e retorna tokens JWT")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        log.info("Login attempt for user: {}", request.getUsername());

        if (authService.isAccountLocked(request.getUsername())) {
            log.warn("Account locked: {}", request.getUsername());
            return ResponseEntity.status(423)
                    .body(ApiResponseDTO.error("Conta bloqueada temporariamente. Tente novamente em 15 minutos."));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            Set<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            String accessToken = tokenProvider.generateAccessToken(request.getUsername(), roles);
            String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());

            authService.recordLoginAttempt(request.getUsername(), true);

            AuthResponseDTO response = new AuthResponseDTO(
                    accessToken,
                    refreshToken,
                    tokenProvider.getAccessTokenValidity(),
                    request.getUsername()
            );

            log.info("User '{}' logged in successfully", request.getUsername());
            return ResponseEntity.ok(ApiResponseDTO.ok("Login realizado com sucesso", response));
        } catch (Exception e) {
            authService.recordLoginAttempt(request.getUsername(), false);
            throw e;
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token de acesso", description = "Usa o refresh token para obter novo access token")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> refresh(@RequestBody RefreshTokenRequestDTO request) {
        log.info("Token refresh attempt");

        String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());
        String username = tokenProvider.getUsernameFromToken(request.getRefreshToken());

        AuthResponseDTO response = new AuthResponseDTO(
                newAccessToken,
                request.getRefreshToken(),
                tokenProvider.getAccessTokenValidity(),
                username
        );

        return ResponseEntity.ok(ApiResponseDTO.ok("Token renovado com sucesso", response));
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<ApiResponseDTO<UsuarioEntity>> register(@RequestBody LoginRequestDTO request) {
        log.info("Register attempt for user: {}", request.getUsername());

        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Username já está em uso"));
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNome(request.getUsername());
        usuario.setAtivo(true);
        usuario.getRoles().add("ROLE_USER");

        usuarioRepository.save(usuario);
        log.info("User '{}' registered successfully", request.getUsername());

        return ResponseEntity.ok(ApiResponseDTO.ok("Usuário registrado com sucesso", usuario));
    }

    @GetMapping("/me")
    @Operation(summary = "Usuário atual", description = "Retorna informações do usuário autenticado")
    public ResponseEntity<ApiResponseDTO<String>> me(Authentication authentication) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Usuário autenticado", authentication.getName()));
    }
}
