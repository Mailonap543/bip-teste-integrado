package com.example.ejb;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeneficioEjbServiceTest {

    private BeneficioEjbService service;

    @BeforeEach
    void setup() {
        service = new BeneficioEjbService();
        service.initTestData();
    }

    @Test
    void testTransferenciaValida() {
        long origemId = 1L;
        long destinoId = 2L;
        double valor = 100.0;

        double saldoOrigemAntes = service.getSaldo(origemId);
        double saldoDestinoAntes = service.getSaldo(destinoId);

        service.transferir(origemId, destinoId, valor);

        assertEquals(saldoOrigemAntes - valor, service.getSaldo(origemId));
        assertEquals(saldoDestinoAntes + valor, service.getSaldo(destinoId));
    }

    @Test
    void testTransferenciaSaldoInsuficiente() {
        long origemId = 1L;
        long destinoId = 2L;
        double valor = 10000.0; // maior que saldo

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.transferir(origemId, destinoId, valor);
        });

        assertTrue(exception.getMessage().contains("saldo insuficiente"));
    }

    @Test
    void testRollbackEmErro() {
        long origemId = 1L;
        long destinoId = 999L;
        double valor = 50.0;

        double saldoAntes = service.getSaldo(origemId);

        assertThrows(RuntimeException.class, () -> service.transferir(origemId, destinoId, valor));

        assertEquals(saldoAntes, service.getSaldo(origemId));
    }
}
