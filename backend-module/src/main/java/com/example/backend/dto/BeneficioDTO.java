package com.example.backend.dto;

import com.example.backend.validation.OnCreate;
import com.example.backend.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO para operacoes com Beneficio")
public class BeneficioDTO {

    @Schema(description = "ID do beneficio", example = "1")
    @NotNull(message = "{beneficio.id.required}", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "Nome do titular", example = "Maria Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{beneficio.nome.notblank}", groups = {OnCreate.class, OnUpdate.class})
    @Size(min = 2, max = 100, message = "{beneficio.nome.size}", groups = {OnCreate.class, OnUpdate.class})
    private String nome;

    @Schema(description = "Descricao do beneficio", example = "Vale Alimentacao")
    @Size(max = 500, message = "{beneficio.descricao.size}", groups = {OnCreate.class, OnUpdate.class})
    private String descricao;

    @Schema(description = "Saldo disponivel", example = "3000.00")
    @NotNull(message = "{beneficio.saldo.required}", groups = OnCreate.class)
    @DecimalMin(value = "0.0", message = "{beneficio.saldo.min}", groups = {OnCreate.class, OnUpdate.class})
    private Double saldo;

    @Schema(description = "Status ativo do beneficio", example = "true")
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
