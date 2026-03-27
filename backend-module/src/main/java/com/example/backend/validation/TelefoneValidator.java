package com.example.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<Telefone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        String digits = value.replaceAll("[^0-9]", "");
        return digits.length() >= 10 && digits.length() <= 11;
    }
}
