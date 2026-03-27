package com.example.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.length() != 11) return false;
        if (digits.matches("(\\d)\\1{10}")) return false;

        int sum = 0;
        for (int i = 0; i < 9; i++) sum += (digits.charAt(i) - '0') * (10 - i);
        int remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        if (remainder != (digits.charAt(9) - '0')) return false;

        sum = 0;
        for (int i = 0; i < 10; i++) sum += (digits.charAt(i) - '0') * (11 - i);
        remainder = (sum * 10) % 11;
        if (remainder == 10) remainder = 0;
        return remainder == (digits.charAt(10) - '0');
    }
}
