package com.example.backend.entity;


import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSFERENCIA")
public class TransferenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CONTA_ORIGEM_ID", nullable = false)
    private Long contaOrigemId;

    @Column(name = "CONTA_DESTINO_ID", nullable = false)
    private Long contaDestinoId;

    @Column(nullable = false, precision = 15, scale = 2)
    private Double valor;

    @Column(name = "DATA_TRANSFERENCIA", nullable = false)
    private LocalDateTime dataTransferencia;

    public TransferenceEntity() {}

    public TransferenceEntity(Long contaOrigemId, Long contaDestinoId, Double valor) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.valor = valor;
        this.dataTransferencia = LocalDateTime.now();
    }


    public Long getId() { return id; }
    public Long getContaOrigemId() { return contaOrigemId; }
    public Long getContaDestinoId() { return contaDestinoId; }
    public Double getValor() { return valor; }
    public LocalDateTime getDataTransferencia() { return dataTransferencia; }


    public void setId(Long id) { this.id = id; }
    public void setContaOrigemId(Long contaOrigemId) { this.contaOrigemId = contaOrigemId; }
    public void setContaDestinoId(Long contaDestinoId) { this.contaDestinoId = contaDestinoId; }
    public void setValor(Double valor) { this.valor = valor; }
    public void setDataTransferencia(LocalDateTime dataTransferencia) { this.dataTransferencia = dataTransferencia; }

    public void setFromId(Long id) {
    }

    public void setToId(Long id) {
    }

    public void setAmount(Double amount) {
    }
}
