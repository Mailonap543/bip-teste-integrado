package com.example.backend.controller;

import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios v1", description = "API v1 para gestão de benefícios")
public class BeneficioController {

    private static final Logger log = LoggerFactory.getLogger(BeneficioController.class);
    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os benefícios")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<ApiResponseDTO<List<BeneficioEntity>>> listAll() {
        log.info("Listing all beneficios");
        List<BeneficioEntity> list = service.listAll();
        return ResponseEntity.ok(ApiResponseDTO.ok("Benefícios listados com sucesso", list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter benefício por ID")
    public ResponseEntity<ApiResponseDTO<BeneficioEntity>> get(
            @Parameter(description = "ID do benefício") @PathVariable Long id) {
        log.info("Getting beneficio by id: {}", id);
        BeneficioEntity b = service.get(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Benefício encontrado", b));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Criar novo benefício")
    @ApiResponse(responseCode = "201", description = "Benefício criado")
    public ResponseEntity<ApiResponseDTO<BeneficioEntity>> create(@Valid @RequestBody BeneficioDTO dto) {
        log.info("Creating beneficio: {}", dto.getNome());
        BeneficioEntity b = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Benefício criado com sucesso", b));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Atualizar benefício")
    public ResponseEntity<ApiResponseDTO<BeneficioEntity>> update(
            @PathVariable Long id, @Valid @RequestBody BeneficioDTO dto) {
        log.info("Updating beneficio id: {}", id);
        BeneficioEntity b = service.update(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Benefício atualizado com sucesso", b));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover benefício")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id) {
        log.info("Deleting beneficio id: {}", id);
        service.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Benefício removido com sucesso", null));
    }

    @PostMapping("/transfer/{origemId}/{destinoId}/{valor}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Transferir valor entre benefícios")
    public ResponseEntity<ApiResponseDTO<Void>> transfer(
            @PathVariable Long origemId,
            @PathVariable Long destinoId,
            @PathVariable Double valor) {
        log.info("Transfer request: from {} to {} amount {}", origemId, destinoId, valor);
        service.transfer(origemId, destinoId, valor);
        return ResponseEntity.ok(ApiResponseDTO.ok("Transferência realizada com sucesso", null));
    }
}
