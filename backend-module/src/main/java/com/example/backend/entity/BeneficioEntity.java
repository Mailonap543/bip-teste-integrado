package com.example.backend.entity;

import jakarta.persistence.*;
import org.springframework.http.ResponseEntity; // Mantido
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections; // Novo import para findAll()
import java.util.List;
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

    // Campo correto: BigDecimal
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean ativa = true;


    // Construtor Padrão (necessário pelo JPA)
    public BeneficioEntity() {
    }


    // Construtor com argumentos
    public BeneficioEntity(String titular, BigDecimal valor, Boolean ativa) {
        this.titular = titular;
        this.valor = valor;
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

    // CORREÇÃO ESSENCIAL: Tipo de retorno deve ser BigDecimal
    public BigDecimal getValor() {
        return valor;
    }

    // CORREÇÃO ESSENCIAL: Tipo de parâmetro deve ser BigDecimal
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


    public void setNome(String bolsaFamília) {
        this.titular = bolsaFamília;
    }


    public String getNome() {
        return this.titular;
    }


    public ResponseEntity<BeneficioEntity> create(BeneficioEntity novo) {
        return ResponseEntity.ok(novo);
    }


    public void setSaldo(BigDecimal bigDecimal) {
        this.valor = bigDecimal;
    }


    public static ResponseEntity<List<BeneficioEntity>> findAll() {
        return ResponseEntity.ok(Collections.emptyList());
    }


    public void setDescricao(String ajudaMensal) {

    }

    public BigDecimal getSaldo() {

        return this.valor;
    }
}