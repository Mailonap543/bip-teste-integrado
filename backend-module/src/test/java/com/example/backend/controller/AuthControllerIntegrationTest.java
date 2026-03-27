package com.example.backend.controller;

import com.example.backend.dto.LoginRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String accessToken;
    private static String refreshToken;

    @Test
    @Order(1)
    void shouldRegisterUser() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("integrationuser");
        request.setPassword("integrationpass123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(2)
    void shouldLoginAndGetTokens() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("integrationuser");
        request.setPassword("integrationpass123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        accessToken = objectMapper.readTree(response).path("data").path("accessToken").asText();
        refreshToken = objectMapper.readTree(response).path("data").path("refreshToken").asText();
    }

    @Test
    @Order(3)
    void shouldRefreshToken() throws Exception {
        String body = "{\"refreshToken\":\"" + refreshToken + "\"}";

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    @Order(4)
    void shouldGetCurrentUser() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("integrationuser"));
    }

    @Test
    @Order(5)
    void shouldRejectUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(6)
    void shouldReturnHealthEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void shouldReturnSwaggerDocs() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void shouldRejectDuplicateRegistration() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("integrationuser");
        request.setPassword("anotherpass");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
