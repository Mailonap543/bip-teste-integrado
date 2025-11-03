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
        dto.setNome("Bolsa Família");
        dto.setValor(500.0);
        dto.setAtiva(true);

        BeneficioEntity entitySalvo = new BeneficioEntity();
        entitySalvo.setTitular(dto.getNome());
        entitySalvo.setValor(BigDecimal.valueOf(dto.getValor()));
        entitySalvo.setAtiva(dto.getAtiva());

        when(beneficioRepository.save(any(BeneficioEntity.class))).thenReturn(entitySalvo);

        BeneficioEntity resultado = beneficioService.create(dto);

        assertNotNull(resultado);
        assertEquals("Bolsa Família", resultado.getTitular());
        assertEquals(BigDecimal.valueOf(500.0), resultado.getValor());
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
        dto.setNome("Auxílio Gás");
        dto.setValor(300.0);
        dto.setAtiva(false);

        BeneficioEntity existente = new BeneficioEntity();
        existente.setTitular("Bolsa Família");
        existente.setValor(BigDecimal.valueOf(500.0));
        existente.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(beneficioRepository.save(any(BeneficioEntity.class))).thenReturn(existente);

        BeneficioEntity resultado = beneficioService.update(1L, dto);

        assertEquals("Auxílio Gás", resultado.getTitular());
        assertEquals(BigDecimal.valueOf(300.0).setScale(1), resultado.getValor().setScale(1));
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
        origem.setValor(BigDecimal.valueOf(500.0));
        origem.setAtiva(true);

        BeneficioEntity destino = new BeneficioEntity();
        destino.setId(2L);
        destino.setValor(BigDecimal.valueOf(200.0));
        destino.setAtiva(true);

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(beneficioRepository.save(any(BeneficioEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(transferenceRepository.save(any(TransferenceEntity.class))).thenAnswer(i -> i.getArgument(0));

        beneficioService.transfer(1L, 2L, 100.0);

        assertEquals(BigDecimal.valueOf(400.0).setScale(1), origem.getValor().setScale(1));
        assertEquals(BigDecimal.valueOf(300.0).setScale(1), destino.getValor().setScale(1));

        verify(beneficioRepository, times(2)).save(any(BeneficioEntity.class));
        verify(transferenceRepository, times(1)).save(any(TransferenceEntity.class));
    }

    @Test
    void deveLancarErroQuandoSaldoInsuficiente() {
        BeneficioEntity origem = new BeneficioEntity();
        origem.setId(1L);
        origem.setValor(BigDecimal.valueOf(50.0));
        origem.setAtiva(true);

        BeneficioEntity destino = new BeneficioEntity();
        destino.setId(2L);
        destino.setValor(BigDecimal.valueOf(200.0));
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
        entity.setTitular("Bolsa Família");

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(entity));

        BeneficioEntity resultado = beneficioService.get(1L);
        assertEquals("Bolsa Família", resultado.getTitular());
    }

    @Test
    void deveLancarErroAoBuscarBeneficioInexistente() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () -> beneficioService.get(1L));
        assertEquals("Benefício não encontrado: 1", ex.getMessage());
    }
}