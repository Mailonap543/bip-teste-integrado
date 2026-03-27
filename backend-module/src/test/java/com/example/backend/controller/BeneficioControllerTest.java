package com.example.backend.controller;

import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.service.BeneficioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(BeneficioController.class)
class BeneficioControllerTest {

    @Autowired
    private BeneficioController beneficioController;

    @MockBean
    private BeneficioService beneficioService;

    @Test
    void deveListarTodosBeneficios() {
        BeneficioEntity b1 = new BeneficioEntity();
        b1.setTitular("Maria Silva");
        b1.setSaldo(BigDecimal.valueOf(3000.0));
        b1.setAtiva(true);

        List<BeneficioEntity> lista = new ArrayList<>();
        lista.add(b1);

        when(beneficioService.listAll()).thenReturn(lista);

        ResponseEntity<ApiResponseDTO<List<BeneficioEntity>>> response = beneficioController.listAll();

        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("Maria Silva", response.getBody().getData().get(0).getTitular());
        verify(beneficioService, times(1)).listAll();
    }

    @Test
    void deveCriarBeneficio() {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Maria Silva");
        dto.setSaldo(3000.0);
        dto.setAtiva(true);

        BeneficioEntity novo = new BeneficioEntity();
        novo.setTitular("Maria Silva");
        novo.setSaldo(BigDecimal.valueOf(3000));
        novo.setAtiva(true);

        when(beneficioService.create(any(BeneficioDTO.class))).thenReturn(novo);

        ResponseEntity<ApiResponseDTO<BeneficioEntity>> response = beneficioController.create(dto);

        assertTrue(response.getBody().isSuccess());
        assertEquals("Maria Silva", response.getBody().getData().getTitular());
        verify(beneficioService, times(1)).create(any(BeneficioDTO.class));
    }

    @Test
    void deveAtualizarBeneficio() {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Joao Santos");
        dto.setSaldo(2000.0);
        dto.setAtiva(true);

        BeneficioEntity atualizado = new BeneficioEntity();
        atualizado.setTitular("Joao Santos");
        atualizado.setSaldo(BigDecimal.valueOf(2000));
        atualizado.setAtiva(true);

        when(beneficioService.update(eq(1L), any(BeneficioDTO.class))).thenReturn(atualizado);

        ResponseEntity<ApiResponseDTO<BeneficioEntity>> response = beneficioController.update(1L, dto);

        assertTrue(response.getBody().isSuccess());
        assertEquals("Joao Santos", response.getBody().getData().getTitular());
        verify(beneficioService, times(1)).update(eq(1L), any(BeneficioDTO.class));
    }

    @Test
    void deveDeletarBeneficio() {
        doNothing().when(beneficioService).delete(1L);

        ResponseEntity<ApiResponseDTO<Void>> response = beneficioController.delete(1L);

        assertTrue(response.getBody().isSuccess());
        verify(beneficioService, times(1)).delete(1L);
    }

    @Test
    void deveTransferirValor() {
        doNothing().when(beneficioService).transfer(1L, 2L, 100.0);

        ResponseEntity<ApiResponseDTO<Void>> response = beneficioController.transfer(1L, 2L, 100.0);

        assertTrue(response.getBody().isSuccess());
        verify(beneficioService, times(1)).transfer(1L, 2L, 100.0);
    }
}
