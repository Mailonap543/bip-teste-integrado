package com.example.backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "BENEFICIO")
public class BeneficioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titular;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean ativa = true;


    // 1. CONSTRUTOR PADRÃO (OBRIGATÓRIO PELO JPA)
    public BeneficioEntity() {
    }


    // 2. CONSTRUTOR CORRIGIDO: Inicializa os campos
    public BeneficioEntity(String titular, BigDecimal valor, Boolean ativa) {
        this.titular = titular;
        this.valor = valor;
        this.ativa = ativa;
    }

    // 3. MÉTODO REMOVIDO: O método 'public static List<BeneficioEntity> findAll()'
    // foi removido. Entidades não devem conter métodos de busca de dados;
    // essa é uma responsabilidade do Repositório (BeneficioRepository).

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

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeneficioEntity that = (BeneficioEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BeneficioEntity{" +
                "id=" + id +
                ", titular='" + titular + '\'' +
                ", valor=" + valor +
                ", ativa=" + ativa +
                '}';
    }

}