package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição de refresh token")
public class RefreshTokenRequestDTO {

    @Schema(description = "Token de refresh")
    private String refreshToken;

    public RefreshTokenRequestDTO() {}

    public RefreshTokenRequestDTO(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
