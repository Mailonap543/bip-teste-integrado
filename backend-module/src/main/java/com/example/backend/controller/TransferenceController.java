package com.example.backend.controller;

import com.example.backend.dto.BeneficioTransferRequestDTO;
import com.example.backend.service.TransferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transferencias")
@Tag(name = "Transferências", description = "API para operações de transferência")
public class TransferenceController {

    private final TransferenceService transferenceService;

    public TransferenceController(TransferenceService transferenceService) {
        this.transferenceService = transferenceService;
    }

    @PostMapping
    @Operation(summary = "Realizar transferência", description = "Transfere valor entre dois benefícios via request body")
    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro na transferência")
    public ResponseEntity<String> transferir(@RequestBody BeneficioTransferRequestDTO request) {
        transferenceService.transferir(
                request.getFromId(),
                request.getToId(),
                BigDecimal.valueOf(request.getAmount())
        );
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }

    @GetMapping("/teste")
    @Operation(summary = "Health check", description = "Verifica se o controller está ativo")
    @ApiResponse(responseCode = "200", description = "Controller ativo")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Controller de Transferência ativo");
    }
}
