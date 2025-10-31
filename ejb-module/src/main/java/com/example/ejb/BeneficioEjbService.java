package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {


    @PersistenceContext
    private EntityManager em;



    public void transfer(Long fromId, Long toId, BigDecimal amount) {


        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }


        Beneficio from = em.find(Beneficio.class, fromId);
        em.lock(from, LockModeType.PESSIMISTIC_WRITE);


        Beneficio to = em.find(Beneficio.class, toId);
        em.lock(to, LockModeType.PESSIMISTIC_WRITE);


        if (from.getValor().compareTo(amount) < 0) {

            throw new IllegalStateException("Saldo insuficiente para a transferência na conta " + fromId);
        }


        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));



        em.merge(from);
        em.merge(to);


    }
}