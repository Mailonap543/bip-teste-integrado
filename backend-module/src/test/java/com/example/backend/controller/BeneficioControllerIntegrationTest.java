package com.example.backend.controller;

import com.example.backend.dto.BeneficioDTO;
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
class BeneficioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String accessToken;

    @Test
    @Order(1)
    void shouldRegisterUser() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("testuser");
        request.setPassword("testpass123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(2)
    void shouldLoginAndGetToken() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("testuser");
        request.setPassword("testpass123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        accessToken = objectMapper.readTree(response).path("data").path("accessToken").asText();
    }

    @Test
    @Order(3)
    void shouldCreateBeneficio() throws Exception {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Maria Silva");
        dto.setSaldo(3000.0);
        dto.setAtiva(true);

        mockMvc.perform(post("/api/v1/beneficios")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.titular").value("Maria Silva"));
    }

    @Test
    @Order(4)
    void shouldListBeneficios() throws Exception {
        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(5)
    void shouldListBeneficiosPaged() throws Exception {
        mockMvc.perform(get("/api/v1/beneficios/paged")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "id")
                        .param("direction", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page").value(0));
    }

    @Test
    @Order(6)
    void shouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/beneficios/99999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @Order(7)
    void shouldRejectUnauthorizedCreate() throws Exception {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Test");
        dto.setSaldo(100.0);

        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    void shouldRefreshToken() throws Exception {
        LoginRequestDTO loginReq = new LoginRequestDTO();
        loginReq.setUsername("testuser");
        loginReq.setPassword("testpass123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andReturn();

        String refreshToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .path("data").path("refreshToken").asText();

        String refreshBody = "{\"refreshToken\":\"" + refreshToken + "\"}";

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    @Order(9)
    void shouldReturnHealthEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void shouldReturnSwaggerDocs() throws Exception {
        mockMvc.perform(get("/api-docs"))
                .andExpect(status().isOk());
    }
}
