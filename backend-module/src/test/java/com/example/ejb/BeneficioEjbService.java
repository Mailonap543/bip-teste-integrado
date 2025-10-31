package com.example.ejb;


import java.util.HashMap;
import java.util.Map;

public class BeneficioEjbService {

    // Simulando um banco de dados em memória
    private Map<Long, Double> contas = new HashMap<>();

    /**
     * Inicializa dados de teste
     */
    public void initTestData() {
        contas.put(1L, 1000.0);
        contas.put(2L, 500.0);
        contas.put(3L, 200.0);
        System.out.println("Dados de teste inicializados: " + contas);
    }


    public void transferir(long origemId, long destinoId, double valor) {
        if (!contas.containsKey(origemId)) {
            throw new IllegalArgumentException("Conta de origem não encontrada: " + origemId);
        }
        if (!contas.containsKey(destinoId)) {
            throw new IllegalArgumentException("Conta de destino não encontrada: " + destinoId);
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser maior que zero.");
        }

        double saldoOrigem = contas.get(origemId);
        if (saldoOrigem < valor) {
            throw new IllegalArgumentException("Saldo insuficiente na conta de origem.");
        }

        contas.put(origemId, saldoOrigem - valor);
        contas.put(destinoId, contas.get(destinoId) + valor);

        System.out.println("Transferência realizada: " + valor + " de " + origemId + " para " + destinoId);
    }


    public double getSaldo(long contaId) {
        if (!contas.containsKey(contaId)) {
            throw new IllegalArgumentException("Conta não encontrada: " + contaId);
        }
        return contas.get(contaId);
    }
}
