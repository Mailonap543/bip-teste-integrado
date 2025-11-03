package com.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode; // Importa RoundingMode para melhor prática

@Entity
@Table(name = "TRANSFERENCIA")
public class TransferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos de origem e destino
    @Column(nullable = false)
    private Long fromId;

    @Column(nullable = false)
    private Long toId;

    // Valor da transferência
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    // Construtor Padrão (necessário pelo JPA)
    public TransferenceEntity() {
    }

    // Construtor usado no Service
    public TransferenceEntity(Long fromId, Long toId, Double amount) {
        this.fromId = fromId;
        this.toId = toId;
        // Usa RoundingMode.HALF_UP para arredondamento padrão
        this.amount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}