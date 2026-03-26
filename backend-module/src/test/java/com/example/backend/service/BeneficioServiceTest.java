package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.entity.TransferenceEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @Mock
    private TransferenceRepository transferenceRepository;

    @InjectMocks
    private BeneficioService beneficioService;

    @Test
    void deveCriarBeneficio() {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Maria Silva");
        dto.setSaldo(3000.0);
        dto.setAtiva(true);

        BeneficioEntity entitySalvo = new BeneficioEntity();
        entitySalvo.setTitular(dto.getNome());
        entitySalvo.setSaldo(BigDecimal.valueOf(dto.getSaldo()));
        entitySalvo.setAtiva(dto.getAtiva());

        when(beneficioRepository.save(any(BeneficioEntity.class))).thenReturn(entitySalvo);

        BeneficioEntity resultado = beneficioService.create(dto);

        assertNotNull(resultado);
        assertEquals("Maria Silva", resultado.getTitular());
        assertEquals(BigDecimal.valueOf(3000.0), resultado.getSaldo());
        assertTrue(resultado.getAtiva());

        verify(beneficioRepository, times(1)).save(any(BeneficioEntity.class));
    }

    @Test
    void deveListarTodosBeneficios() {
        List<BeneficioEntity> lista = new ArrayList<>();
        lista.add(new BeneficioEntity());
        lista.add(new BeneficioEntity());

        when(beneficioRepository.findAll()).thenReturn(lista);

        List<BeneficioEntity> resultado = beneficioService.listAll();

        assertEquals(2, resultado.size());
        verify(beneficioRepository, times(1)).findAll();
    }

    @Test
    void deveAtualizarBeneficio() {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("João Santos");
        dto.setSaldo(2000.0);
        dto.setAtiva(false);

        BeneficioEntity existente = new BeneficioEntity();
        existente.setTitular("Maria Silva");
        existente.setSaldo(BigDecimal.valueOf(3000.0));
        existente.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(beneficioRepository.save(any(BeneficioEntity.class))).thenReturn(existente);

        BeneficioEntity resultado = beneficioService.update(1L, dto);

        assertEquals("João Santos", resultado.getTitular());
        assertEquals(BigDecimal.valueOf(2000.0).setScale(1), resultado.getSaldo().setScale(1));
        assertFalse(resultado.getAtiva());

        verify(beneficioRepository, times(1)).findById(1L);
        verify(beneficioRepository, times(1)).save(existente);
    }

    @Test
    void deveDeletarBeneficio() {
        BeneficioEntity dummyEntity = new BeneficioEntity();
        dummyEntity.setId(1L);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(dummyEntity));
        doNothing().when(beneficioRepository).deleteById(1L);

        assertDoesNotThrow(() -> beneficioService.delete(1L));
        verify(beneficioRepository, times(1)).findById(1L);
        verify(beneficioRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarErroAoDeletarBeneficioInexistente() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> beneficioService.delete(1L));
        assertEquals("Benefício não encontrado: 1", ex.getMessage());

        verify(beneficioRepository, never()).deleteById(anyLong());
        verify(beneficioRepository, times(1)).findById(1L);
    }

    @Test
    void deveTransferirEntreBeneficios() {
        BeneficioEntity origem = new BeneficioEntity();
        origem.setId(1L);
        origem.setSaldo(BigDecimal.valueOf(500.0));
        origem.setAtiva(true);

        BeneficioEntity destino = new BeneficioEntity();
        destino.setId(2L);
        destino.setSaldo(BigDecimal.valueOf(200.0));
        destino.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(beneficioRepository.save(any(BeneficioEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(transferenceRepository.save(any(TransferenceEntity.class))).thenAnswer(i -> i.getArgument(0));

        beneficioService.transfer(1L, 2L, 100.0);

        assertEquals(BigDecimal.valueOf(400.0).setScale(1), origem.getSaldo().setScale(1));
        assertEquals(BigDecimal.valueOf(300.0).setScale(1), destino.getSaldo().setScale(1));

        verify(beneficioRepository, times(2)).save(any(BeneficioEntity.class));
        verify(transferenceRepository, times(1)).save(any(TransferenceEntity.class));
    }

    @Test
    void deveLancarErroQuandoSaldoInsuficiente() {
        BeneficioEntity origem = new BeneficioEntity();
        origem.setId(1L);
        origem.setSaldo(BigDecimal.valueOf(50.0));
        origem.setAtiva(true);

        BeneficioEntity destino = new BeneficioEntity();
        destino.setId(2L);
        destino.setSaldo(BigDecimal.valueOf(200.0));
        destino.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> beneficioService.transfer(1L, 2L, 100.0));

        assertEquals("Saldo insuficiente no benefício de origem.", ex.getMessage());

        verify(beneficioRepository, never()).save(any(BeneficioEntity.class));
    }

    @Test
    void deveObterBeneficioPorId() {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setId(1L);
        entity.setTitular("Maria Silva");

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(entity));

        BeneficioEntity resultado = beneficioService.get(1L);
        assertEquals("Maria Silva", resultado.getTitular());
    }

    @Test
    void deveLancarErroAoBuscarBeneficioInexistente() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> beneficioService.get(1L));
        assertEquals("Benefício não encontrado: 1", ex.getMessage());
    }
}
