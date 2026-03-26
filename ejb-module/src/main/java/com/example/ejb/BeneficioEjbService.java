package com.example.ejb;

import com.example.ejb.entity.Beneficio;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Transfere valor entre dois benefícios com validações e pessimistic locking.
     * CORREÇÃO: Agora verifica saldo, usa locking, valida benefícios ativos.
     */
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }

        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("IDs de origem e destino são obrigatórios.");
        }

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Origem e destino não podem ser iguais.");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio to = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);

        if (from == null) {
            throw new IllegalArgumentException("Benefício de origem não encontrado: " + fromId);
        }
        if (to == null) {
            throw new IllegalArgumentException("Benefício de destino não encontrado: " + toId);
        }

        if (Boolean.FALSE.equals(from.getAtiva())) {
            throw new IllegalArgumentException("Benefício de origem está inativo.");
        }

        if (Boolean.FALSE.equals(to.getAtiva())) {
            throw new IllegalArgumentException("Benefício de destino está inativo.");
        }

        if (from.getSaldo().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente. Disponível: " + from.getSaldo() + ", Solicitado: " + amount);
        }

        from.setSaldo(from.getSaldo().subtract(amount).setScale(2, RoundingMode.HALF_UP));
        to.setSaldo(to.getSaldo().add(amount).setScale(2, RoundingMode.HALF_UP));

        em.flush();
    }
}
