package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para requisição de transferência")
public class BeneficioTransferRequestDTO {

    @Schema(description = "ID do benefício de origem", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fromId;

    @Schema(description = "ID do benefício de destino", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long toId;

    @Schema(description = "Valor a ser transferido", example = "300.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double amount;

    public Long getFromId() { return fromId; }
    public void setFromId(Long fromId) { this.fromId = fromId; }

    public Long getToId() { return toId; }
    public void setToId(Long toId) { this.toId = toId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
