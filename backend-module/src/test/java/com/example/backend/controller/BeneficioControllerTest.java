package com.example.backend.controller;

import com.example.backend.comtroller.BeneficioController;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.service.BeneficioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BeneficioControllerTest {

    @Mock
    private BeneficioService beneficioService;

    @InjectMocks
    private BeneficioController beneficioController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodosBeneficios() {
        BeneficioEntity b1 = new BeneficioEntity();
        b1.setTitular("Bolsa Família");
        b1.setValor(BigDecimal.valueOf(100.0));
        b1.setAtiva(true);

        BeneficioEntity b2 = new BeneficioEntity();
        b2.setTitular("Auxílio Gás");
        b2.setValor(BigDecimal.valueOf(50.0));
        b2.setAtiva(true);

        List<BeneficioEntity> lista = new ArrayList<>();
        lista.add(b1);
        lista.add(b2);

        when(beneficioService.listAll()).thenReturn(lista);

        ResponseEntity<List<BeneficioEntity>> response = beneficioController.listAll();

        assertEquals(2, response.getBody().size());
        assertEquals("Bolsa Família", response.getBody().get(0).getTitular());
        assertEquals("Auxílio Gás", response.getBody().get(1).getTitular());
        verify(beneficioService, times(1)).listAll();
    }

    @Test
    void deveCriarBeneficio() {

        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Vale Alimentação");
        dto.setValor(500.0);
        dto.setAtiva(true);


        BeneficioEntity novo = new BeneficioEntity();
        novo.setTitular("Vale Alimentação");
        novo.setValor(BigDecimal.valueOf(500));
        novo.setAtiva(true);

        when(beneficioService.create(any(BeneficioDTO.class))).thenReturn(novo);

        ResponseEntity<BeneficioEntity> response = beneficioController.create(dto);

        assertNotNull(response.getBody());
        assertEquals("Vale Alimentação", response.getBody().getTitular());
        assertEquals(BigDecimal.valueOf(500), response.getBody().getValor());
        verify(beneficioService, times(1)).create(any(BeneficioDTO.class));
    }

    @Test
    void deveAtualizarBeneficio() {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Vale Transporte");
        dto.setValor(200.0);
        dto.setAtiva(true);

        BeneficioEntity atualizado = new BeneficioEntity();
        atualizado.setTitular("Vale Transporte");
        atualizado.setValor(BigDecimal.valueOf(200));
        atualizado.setAtiva(true);

        when(beneficioService.update(eq(1L), any(BeneficioDTO.class))).thenReturn(atualizado);

        ResponseEntity<BeneficioEntity> response = beneficioController.update(1L, dto);

        assertNotNull(response.getBody());
        assertEquals("Vale Transporte", response.getBody().getTitular());
        assertEquals(BigDecimal.valueOf(200), response.getBody().getValor());
        verify(beneficioService, times(1)).update(eq(1L), any(BeneficioDTO.class));
    }

    @Test
    void deveDeletarBeneficio() {
        doNothing().when(beneficioService).delete(1L);

        ResponseEntity<Void> response = beneficioController.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(beneficioService, times(1)).delete(1L);
    }

    @Test
    void deveTransferirValor() {
        doNothing().when(beneficioService).transfer(1L, 2L, 100.0);

        ResponseEntity<Void> response = beneficioController.transfer(1L, 2L, 100.0);

        assertEquals(200, response.getStatusCodeValue());
        verify(beneficioService, times(1)).transfer(1L, 2L, 100.0);
    }
}
