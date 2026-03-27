package com.example.ejb.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "CONTA")
public class Beneficio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITULAR", nullable = false, length = 100)
    private String titular;

    @Column(name = "SALDO", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(name = "ATIVA", nullable = false)
    private Boolean ativa = true;

    @Version
    @Column(name = "VERSION")
    private Long version;

    public Beneficio() {
    }

    public Beneficio(String titular, BigDecimal saldo, Boolean ativa) {
        this.titular = titular;
        this.saldo = saldo;
        this.ativa = ativa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beneficio that = (Beneficio) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Beneficio{" +
                "id=" + id +
                ", titular='" + titular + '\'' +
                ", saldo=" + saldo +
                ", ativa=" + ativa +
                '}';
    }
}
