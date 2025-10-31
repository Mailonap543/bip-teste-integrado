package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    // O EJB por ser Stateless e ter o @PersistenceContext garante que o EM
    // e o método transfer() rodem dentro de um contexto transacional.
    @PersistenceContext
    private EntityManager em;


    // O EJB já garante o ACID, mas precisamos do locking para concorrência!
    public void transfer(Long fromId, Long toId, BigDecimal amount) {

        // 1. Validação do valor
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da transferência deve ser positivo.");
        }

        // 2. Busca e Bloqueio Pessimista na entidade de ORIGEM.
        // O LockModeType.PESSIMISTIC_WRITE garante que o registro 'from'
        // seja bloqueado no banco de dados, evitando lost updates ou leituras sujas.
        Beneficio from = em.find(Beneficio.class, fromId);
        em.lock(from, LockModeType.PESSIMISTIC_WRITE);

        // A entidade de destino 'to' pode usar um bloqueio menos restritivo (ou nenhum),
        // mas é bom bloquear para evitar Lost Update também,
        // embora o débito seja a operação mais crítica.
        Beneficio to = em.find(Beneficio.class, toId);
        em.lock(to, LockModeType.PESSIMISTIC_WRITE);

        // 3. Validação de Saldo Suficiente
        if (from.getValor().compareTo(amount) < 0) {
            // Lança exceção. O EJB container fará o ROLLBACK automático da transação.
            throw new IllegalStateException("Saldo insuficiente para a transferência na conta " + fromId);
        }

        // 4. Aplicação das Mudanças
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        // 5. Persistência (Flush)
        // O EntityManager gerencia as entidades, e o 'em.merge()' é opcional
        // (a menos que 'from' e 'to' viessem do 'detached' state).
        // Se eles vieram do 'em.find()', eles já estão 'managed', então
        // a transação fará o commit no final, salvando as alterações.
        // Manter o 'merge' é mais seguro caso a entidade tenha sido detachada em algum ponto.
        em.merge(from);
        em.merge(to);

        // O EJB container garante que, ao sair do método SEM exceção,
        // o COMMIT da transção ocorra, liberando os locks e salvando
        // as duas alterações de forma ATÔMICA.
    }
}