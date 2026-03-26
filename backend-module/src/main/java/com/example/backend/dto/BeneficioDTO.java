package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para operações com Benefício")
public class BeneficioDTO {

    @Schema(description = "ID do benefício", example = "1")
    private Long id;

    @Schema(description = "Nome do titular", example = "Maria Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(description = "Descrição do benefício", example = "Vale Alimentação")
    private String descricao;

    @Schema(description = "Saldo disponível", example = "3000.00")
    private Double saldo;

    @Schema(description = "Status ativo do benefício", example = "true")
    private Boolean ativa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
}
