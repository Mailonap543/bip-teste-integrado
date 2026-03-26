package com.example.backend.controller;

import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.BeneficioTransferRequestDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.service.TransferenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferenceController.class)
class TransferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransferenceService transferenceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveRetornarStatusAtivo() throws Exception {
        mockMvc.perform(get("/api/v1/transferencias/teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
