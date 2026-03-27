package com.example.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TelefoneValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Telefone {
    String message() default "Telefone invalido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
