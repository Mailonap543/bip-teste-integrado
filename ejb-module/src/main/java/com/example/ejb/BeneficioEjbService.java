package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import com.example.backend.model.Beneficio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Valor deve ser positivo.");

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio to   = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);

        if (from == null) throw new IllegalArgumentException("Origem não encontrada: " + fromId);
        if (to == null)   throw new IllegalArgumentException("Destino não encontrada: " + toId);

        if (from.getValor().compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insuficiente.");

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.flush();
    }

    public void initTestData() {
    }

    public double getSaldo(long origemId) {

        return 0;
    }

    public void transferir(long origemId, long destinoId, double valor) {
    }
}