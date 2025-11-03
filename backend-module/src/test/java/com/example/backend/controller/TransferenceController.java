package com.example.backend.controller;

import com.example.backend.service.TransferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenceController {

    private final TransferenceService transferenceService;

    public TransferenceController(TransferenceService transferenceService) {
        this.transferenceService = transferenceService;
    }

    @PostMapping
    public ResponseEntity<String> transferir(
            @RequestParam Long fromId,
            @RequestParam Long toId,
            @RequestParam BigDecimal valor) {
        try {
            transferenceService.transferir(fromId, toId, valor);
            return ResponseEntity.ok("Transferência realizada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("TransferenceController ativo!");
    }
}
