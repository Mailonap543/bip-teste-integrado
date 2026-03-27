package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.mapper.BeneficioMapper;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioServiceTest {

    @Mock
    private BeneficioRepository beneficioRepository;

    @Mock
    private TransferenceRepository transferenceRepository;

    @Mock
    private BeneficioMapper beneficioMapper;

    @InjectMocks
    private BeneficioService beneficioService;

    private BeneficioEntity entity;
    private BeneficioDTO dto;

    @BeforeEach
    void setUp() {
        entity = new BeneficioEntity();
        entity.setId(1L);
        entity.setTitular("Maria Silva");
        entity.setSaldo(new BigDecimal("3000.00"));
        entity.setAtiva(true);

        dto = new BeneficioDTO();
        dto.setNome("Maria Silva");
        dto.setSaldo(3000.0);
        dto.setAtiva(true);
    }

    @Nested
    @DisplayName("Create Tests")
    class CreateTests {

        @Test
        @DisplayName("Should create beneficio successfully")
        void shouldCreateBeneficio() {
            when(beneficioMapper.toEntity(dto)).thenReturn(entity);
            when(beneficioRepository.save(any(BeneficioEntity.class))).thenReturn(entity);

            BeneficioEntity result = beneficioService.create(dto);

            assertNotNull(result);
            assertEquals("Maria Silva", result.getTitular());
            verify(beneficioRepository).save(any(BeneficioEntity.class));
        }

        @Test
        @DisplayName("Should set default saldo to zero when null")
        void shouldSetDefaultSaldoWhenNull() {
            dto.setSaldo(null);
            BeneficioEntity entityWithZero = new BeneficioEntity();
            entityWithZero.setSaldo(BigDecimal.ZERO);
            when(beneficioMapper.toEntity(dto)).thenReturn(entityWithZero);
            when(beneficioRepository.save(any())).thenReturn(entityWithZero);

            BeneficioEntity result = beneficioService.create(dto);

            assertEquals(BigDecimal.ZERO, result.getSaldo());
        }
    }

    @Nested
    @DisplayName("List Tests")
    class ListTests {

        @Test
        @DisplayName("Should list all beneficios")
        void shouldListAll() {
            when(beneficioRepository.findAll()).thenReturn(List.of(entity));

            List<BeneficioEntity> result = beneficioService.listAll();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should list paged beneficios")
        void shouldListPaged() {
            Page<BeneficioEntity> page = new PageImpl<>(List.of(entity));
            when(beneficioRepository.findAll(any(PageRequest.class))).thenReturn(page);

            Page<BeneficioEntity> result = beneficioService.listPaged(PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
        }
    }

    @Nested
    @DisplayName("Get Tests")
    class GetTests {

        @Test
        @DisplayName("Should get beneficio by id")
        void shouldGetById() {
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(entity));

            BeneficioEntity result = beneficioService.get(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
        }

        @Test
        @DisplayName("Should throw when beneficio not found")
        void shouldThrowWhenNotFound() {
            when(beneficioRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> beneficioService.get(99L));
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update beneficio successfully")
        void shouldUpdateBeneficio() {
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(beneficioRepository.save(any())).thenReturn(entity);

            BeneficioEntity result = beneficioService.update(1L, dto);

            assertNotNull(result);
            verify(beneficioMapper).updateEntity(entity, dto);
            verify(beneficioRepository).save(entity);
        }

        @Test
        @DisplayName("Should throw when updating non-existent beneficio")
        void shouldThrowWhenUpdatingNonExistent() {
            when(beneficioRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> beneficioService.update(99L, dto));
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete beneficio successfully")
        void shouldDeleteBeneficio() {
            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(entity));

            beneficioService.delete(1L);

            verify(beneficioRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw when deleting non-existent beneficio")
        void shouldThrowWhenDeletingNonExistent() {
            when(beneficioRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> beneficioService.delete(99L));
        }
    }

    @Nested
    @DisplayName("Transfer Tests")
    class TransferTests {

        @Test
        @DisplayName("Should transfer successfully")
        void shouldTransferSuccessfully() {
            BeneficioEntity origem = new BeneficioEntity();
            origem.setId(1L);
            origem.setSaldo(new BigDecimal("1000.00"));
            origem.setAtiva(true);

            BeneficioEntity destino = new BeneficioEntity();
            destino.setId(2L);
            destino.setSaldo(new BigDecimal("500.00"));
            destino.setAtiva(true);

            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));

            beneficioService.transfer(1L, 2L, 200.0);

            assertEquals(new BigDecimal("800.00"), origem.getSaldo());
            assertEquals(new BigDecimal("700.00"), destino.getSaldo());
            verify(beneficioRepository, times(2)).save(any());
            verify(transferenceRepository).save(any());
        }

        @Test
        @DisplayName("Should throw when same origin and destination")
        void shouldThrowWhenSameOriginDestiny() {
            assertThrows(BusinessException.class, () -> beneficioService.transfer(1L, 1L, 100.0));
        }

        @Test
        @DisplayName("Should throw when amount is zero")
        void shouldThrowWhenAmountZero() {
            assertThrows(BusinessException.class, () -> beneficioService.transfer(1L, 2L, 0.0));
        }

        @Test
        @DisplayName("Should throw when amount is null")
        void shouldThrowWhenAmountNull() {
            assertThrows(BusinessException.class, () -> beneficioService.transfer(1L, 2L, null));
        }

        @Test
        @DisplayName("Should throw when insufficient balance")
        void shouldThrowWhenInsufficientBalance() {
            BeneficioEntity origem = new BeneficioEntity();
            origem.setId(1L);
            origem.setSaldo(new BigDecimal("100.00"));
            origem.setAtiva(true);

            BeneficioEntity destino = new BeneficioEntity();
            destino.setId(2L);
            destino.setSaldo(new BigDecimal("500.00"));
            destino.setAtiva(true);

            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));

            assertThrows(BusinessException.class, () -> beneficioService.transfer(1L, 2L, 200.0));
        }

        @Test
        @DisplayName("Should throw when beneficio is inactive")
        void shouldThrowWhenInactive() {
            BeneficioEntity origem = new BeneficioEntity();
            origem.setId(1L);
            origem.setSaldo(new BigDecimal("1000.00"));
            origem.setAtiva(false);

            BeneficioEntity destino = new BeneficioEntity();
            destino.setId(2L);
            destino.setSaldo(new BigDecimal("500.00"));
            destino.setAtiva(true);

            when(beneficioRepository.findById(1L)).thenReturn(Optional.of(origem));
            when(beneficioRepository.findById(2L)).thenReturn(Optional.of(destino));

            assertThrows(BusinessException.class, () -> beneficioService.transfer(1L, 2L, 100.0));
        }
    }
}
