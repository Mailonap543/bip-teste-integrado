package com.example.backend.controller;

import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.BeneficioTransferRequestDTO;
import com.example.backend.service.TransferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/transferencias")
@Tag(name = "Transferências v1", description = "API v1 para operações de transferência")
public class TransferenceController {

    private static final Logger log = LoggerFactory.getLogger(TransferenceController.class);
    private final TransferenceService transferenceService;

    public TransferenceController(TransferenceService transferenceService) {
        this.transferenceService = transferenceService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Realizar transferência", description = "Transfere valor entre dois benefícios")
    public ResponseEntity<ApiResponseDTO<String>> transferir(@RequestBody BeneficioTransferRequestDTO request) {
        log.info("Transfer request: from {} to {} amount {}", request.getFromId(), request.getToId(), request.getAmount());
        transferenceService.transferir(
                request.getFromId(),
                request.getToId(),
                BigDecimal.valueOf(request.getAmount())
        );
        return ResponseEntity.ok(ApiResponseDTO.ok("Transferência realizada com sucesso", "OK"));
    }

    @GetMapping("/teste")
    @Operation(summary = "Health check")
    public ResponseEntity<ApiResponseDTO<String>> teste() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Controller de Transferência ativo", "OK"));
    }
}
