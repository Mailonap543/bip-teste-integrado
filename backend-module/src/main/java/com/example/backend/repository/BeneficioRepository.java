package com.example.backend.repository;

import com.example.backend.entity.BeneficioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BeneficioRepository extends JpaRepository<BeneficioEntity, Long> {
}
