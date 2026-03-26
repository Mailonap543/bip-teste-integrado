package com.example.backend.controller;

import com.example.backend.dto.BeneficioTransferRequestDTO;
import com.example.backend.exception.BusinessException;
import com.example.backend.service.TransferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransferenceController.class)
class TransferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferenceService transferenceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void deveTransferirComSucesso() throws Exception {
        BeneficioTransferRequestDTO request = new BeneficioTransferRequestDTO();
        request.setFromId(1L);
        request.setToId(2L);
        request.setAmount(300.0);

        doNothing().when(transferenceService).transferir(eq(1L), eq(2L), any());

        mockMvc.perform(post("/api/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferência realizada com sucesso"));
    }

    @Test
    void deveRetornarErroQuandoSaldoInsuficiente() throws Exception {
        BeneficioTransferRequestDTO request = new BeneficioTransferRequestDTO();
        request.setFromId(1L);
        request.setToId(2L);
        request.setAmount(99999.0);

        doThrow(new BusinessException("Saldo insuficiente para transferência"))
                .when(transferenceService).transferir(eq(1L), eq(2L), any());

        mockMvc.perform(post("/api/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarStatusAtivo() throws Exception {
        mockMvc.perform(get("/api/transferencias/teste"))
                .andExpect(status().isOk())
                .andExpect(content().string("Controller de Transferência ativo"));
    }
}
