package com.example.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhe de erro de validação de campo")
public class FieldErrorDTO {

    @Schema(description = "Nome do campo com erro")
    private String field;

    @Schema(description = "Mensagem de erro")
    private String message;

    @Schema(description = "Valor rejeitado")
    private Object rejectedValue;

    public FieldErrorDTO() {}

    public FieldErrorDTO(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getRejectedValue() { return rejectedValue; }
    public void setRejectedValue(Object rejectedValue) { this.rejectedValue = rejectedValue; }
}
