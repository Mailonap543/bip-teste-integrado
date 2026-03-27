package com.example.backend.repository;

import com.example.backend.entity.BeneficioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BeneficioRepository extends JpaRepository<BeneficioEntity, Long>, JpaSpecificationExecutor<BeneficioEntity> {

    Page<BeneficioEntity> findByAtiva(Boolean ativa, Pageable pageable);

    Page<BeneficioEntity> findByTitularContainingIgnoreCase(String titular, Pageable pageable);

    List<BeneficioEntity> findByAtivaTrue();

    @Query("SELECT COALESCE(SUM(b.saldo), 0) FROM BeneficioEntity b WHERE b.ativa = true")
    BigDecimal sumSaldoByAtivaTrue();

    @Query("SELECT COUNT(b) FROM BeneficioEntity b WHERE b.ativa = true")
    long countByAtivaTrue();

    @Query("SELECT b.id as id, b.titular as titular, b.saldo as saldo, b.ativa as ativa FROM BeneficioEntity b WHERE b.ativa = true")
    List<BeneficioProjection> findAllActiveProjections();
}
