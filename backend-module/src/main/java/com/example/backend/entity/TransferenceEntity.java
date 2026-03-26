package com.example.backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSFERENCIA")
public class TransferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CONTA_ORIGEM_ID", nullable = false)
    private Long fromId;

    @Column(name = "CONTA_DESTINO_ID", nullable = false)
    private Long toId;

    @Column(name = "VALOR", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "DATA_TRANSFERENCIA")
    private LocalDateTime dataTransferencia;

    @PrePersist
    public void prePersist() {
        if (dataTransferencia == null) {
            dataTransferencia = LocalDateTime.now();
        }
    }

    public TransferenceEntity() {
    }

    public TransferenceEntity(Long fromId, Long toId, BigDecimal amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.dataTransferencia = LocalDateTime.now();
    }

    public TransferenceEntity(Long fromId, Long toId, Double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
        this.dataTransferencia = LocalDateTime.now();
    }

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

    public LocalDateTime getDataTransferencia() {
        return dataTransferencia;
    }

    public void setDataTransferencia(LocalDateTime dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }
}
