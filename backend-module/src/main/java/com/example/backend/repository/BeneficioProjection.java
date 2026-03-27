package com.example.backend.repository;

import java.math.BigDecimal;

public interface BeneficioProjection {
    Long getId();
    String getTitular();
    BigDecimal getSaldo();
    Boolean getAtiva();
}
