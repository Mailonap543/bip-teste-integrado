package com.example.backend.config;

import com.example.backend.dto.FieldErrorDTO;
import com.example.backend.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ERROR = "error";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ERRORS = "errors";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        log.warn("Business error: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "BUSINESS_ERROR", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "AUTH_FAILED", "Credenciais inválidas");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Acesso negado");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorDTO(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
                .collect(Collectors.toList());

        String errorMessage = fieldErrors.stream()
                .map(FieldErrorDTO::getMessage)
                .collect(Collectors.joining(", "));

        log.warn("Validation error: {}", errorMessage);

        Map<String, Object> body = buildBaseResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", errorMessage);
        body.put(KEY_ERRORS, fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Erro interno do servidor");
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(buildBaseResponse(status, error, message));
    }

    private Map<String, Object> buildBaseResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_TIMESTAMP, LocalDateTime.now().toString());
        body.put(KEY_STATUS, status.value());
        body.put(KEY_ERROR, error);
        body.put(KEY_MESSAGE, message);
        return body;
    }
}
