package com.example.backend.service;

import com.example.backend.entity.BeneficioEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferenceServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @Mock
    private TransferenceRepository transferenceRepository;

    @InjectMocks
    private TransferenceService transferenceService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveTransferirSaldoComSucesso() {
        BeneficioEntity origem = new BeneficioEntity();
        origem.setId(1L);
        origem.setNome("Benefício Origem");
        origem.setSaldo(BigDecimal.valueOf(1000));
        origem.setAtiva(true);

        BeneficioEntity destino = new BeneficioEntity();
        destino.setId(2L);
        destino.setNome("Benefício Destino");
        destino.setSaldo(BigDecimal.valueOf(500));
        destino.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(beneficioRepository.save(any(BeneficioEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        BigDecimal valorTransferencia = BigDecimal.valueOf(300);
        transferenceService.transferir(origem.getId(), destino.getId(), valorTransferencia);

        assertEquals(BigDecimal.valueOf(700), origem.getSaldo());
        assertEquals(BigDecimal.valueOf(800), destino.getSaldo());
        verify(beneficioRepository, times(2)).save(any(BeneficioEntity.class));
    }

    @Test
    void deveLancarErroSaldoInsuficiente() {
        BeneficioEntity origem = new BeneficioEntity();
        origem.setId(1L);
        origem.setNome("Origem");
        origem.setSaldo(BigDecimal.valueOf(100));
        origem.setAtiva(true);

        BeneficioEntity destino = new BeneficioEntity();
        destino.setId(2L);
        destino.setNome("Destino");
        destino.setSaldo(BigDecimal.valueOf(200));
        destino.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));

        BigDecimal valorTransferencia = BigDecimal.valueOf(300);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                transferenceService.transferir(origem.getId(), destino.getId(), valorTransferencia)
        );

        assertEquals("Saldo insuficiente para transferência", exception.getMessage());
    }
}
