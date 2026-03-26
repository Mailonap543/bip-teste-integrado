package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação com tokens JWT")
public class AuthResponseDTO {

    @Schema(description = "Token de acesso JWT")
    private String accessToken;

    @Schema(description = "Token de refresh")
    private String refreshToken;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Tempo de expiração em milissegundos")
    private long expiresIn;

    @Schema(description = "Nome de usuário")
    private String username;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String accessToken, String refreshToken, long expiresIn, String username) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.username = username;
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
