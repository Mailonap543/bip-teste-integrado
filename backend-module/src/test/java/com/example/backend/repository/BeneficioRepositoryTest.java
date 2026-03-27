package com.example.backend.repository;

import com.example.backend.entity.BeneficioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;

import static com.example.backend.repository.BeneficioSpecifications.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class BeneficioRepositoryTest {

    @Autowired
    private BeneficioRepository beneficioRepository;

    @BeforeEach
    void setUp() {
        beneficioRepository.deleteAll();

        BeneficioEntity b1 = new BeneficioEntity();
        b1.setTitular("Maria Silva");
        b1.setSaldo(new BigDecimal("3000.00"));
        b1.setAtiva(true);
        beneficioRepository.save(b1);

        BeneficioEntity b2 = new BeneficioEntity();
        b2.setTitular("Joao Santos");
        b2.setSaldo(new BigDecimal("1500.00"));
        b2.setAtiva(true);
        beneficioRepository.save(b2);

        BeneficioEntity b3 = new BeneficioEntity();
        b3.setTitular("Ana Costa");
        b3.setSaldo(new BigDecimal("500.00"));
        b3.setAtiva(false);
        beneficioRepository.save(b3);
    }

    @Test
    @DisplayName("Should find by ativa with pagination")
    void shouldFindByAtiva() {
        Page<BeneficioEntity> page = beneficioRepository.findByAtiva(true, PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
    }

    @Test
    @DisplayName("Should find by titular containing ignoring case")
    void shouldFindByTitularContaining() {
        Page<BeneficioEntity> page = beneficioRepository.findByTitularContainingIgnoreCase("maria", PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertEquals("Maria Silva", page.getContent().get(0).getTitular());
    }

    @Test
    @DisplayName("Should count active beneficios")
    void shouldCountActive() {
        long count = beneficioRepository.countByAtivaTrue();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should sum saldo of active beneficios")
    void shouldSumSaldo() {
        BigDecimal sum = beneficioRepository.sumSaldoByAtivaTrue();

        assertEquals(0, sum.compareTo(new BigDecimal("4500.00")));
    }

    @Test
    @DisplayName("Should find all active as list")
    void shouldFindAllActive() {
        List<BeneficioEntity> active = beneficioRepository.findByAtivaTrue();

        assertEquals(2, active.size());
    }

    @Test
    @DisplayName("Should use specifications for dynamic queries")
    void shouldUseSpecifications() {
        Specification<BeneficioEntity> spec = titularContains("maria")
                .and(ativaEquals(true));

        List<BeneficioEntity> result = beneficioRepository.findAll(spec);

        assertEquals(1, result.size());
        assertEquals("Maria Silva", result.get(0).getTitular());
    }

    @Test
    @DisplayName("Should use specifications for saldo range")
    void shouldUseSaldoSpecification() {
        Specification<BeneficioEntity> spec = saldoGreaterThan(new BigDecimal("1000.00"))
                .and(saldoLessThan(new BigDecimal("4000.00")));

        List<BeneficioEntity> result = beneficioRepository.findAll(spec);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should return projections")
    void shouldReturnProjections() {
        List<BeneficioProjection> projections = beneficioRepository.findAllActiveProjections();

        assertEquals(2, projections.size());
        assertNotNull(projections.get(0).getId());
        assertNotNull(projections.get(0).getTitular());
    }

    @Test
    @DisplayName("Should paginate results")
    void shouldPaginate() {
        Page<BeneficioEntity> page = beneficioRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());
    }

    @Test
    @DisplayName("Should find by id")
    void shouldFindById() {
        List<BeneficioEntity> all = beneficioRepository.findAll();
        Long id = all.get(0).getId();

        assertTrue(beneficioRepository.findById(id).isPresent());
    }
}
