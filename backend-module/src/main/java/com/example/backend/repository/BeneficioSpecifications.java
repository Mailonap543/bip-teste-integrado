package com.example.backend.repository;

import com.example.backend.entity.BeneficioEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class BeneficioSpecifications {

    private BeneficioSpecifications() {}

    public static Specification<BeneficioEntity> titularContains(String titular) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("titular")), "%" + titular.toLowerCase() + "%");
    }

    public static Specification<BeneficioEntity> ativaEquals(Boolean ativa) {
        return (root, query, cb) ->
                cb.equal(root.get("ativa"), ativa);
    }

    public static Specification<BeneficioEntity> saldoGreaterThan(BigDecimal minSaldo) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("saldo"), minSaldo);
    }

    public static Specification<BeneficioEntity> saldoLessThan(BigDecimal maxSaldo) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("saldo"), maxSaldo);
    }
}
