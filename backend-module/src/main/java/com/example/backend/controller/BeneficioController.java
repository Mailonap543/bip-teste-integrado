package com.example.backend.controller;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
@Tag(name = "Benefícios", description = "API para gestão de benefícios")
public class BeneficioController {

    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos os benefícios", description = "Retorna a lista completa de benefícios cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<BeneficioEntity>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter benefício por ID", description = "Retorna um benefício específico pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Benefício encontrado")
    @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    public ResponseEntity<BeneficioEntity> get(
            @Parameter(description = "ID do benefício") @PathVariable Long id) {
        BeneficioEntity b = service.get(id);
        return b == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(b);
    }

    @PostMapping
    @Operation(summary = "Criar novo benefício", description = "Cria um novo benefício no sistema")
    @ApiResponse(responseCode = "201", description = "Benefício criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<BeneficioEntity> create(@RequestBody BeneficioDTO dto) {
        BeneficioEntity b = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(b);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar benefício", description = "Atualiza os dados de um benefício existente")
    @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    public ResponseEntity<BeneficioEntity> update(
            @Parameter(description = "ID do benefício") @PathVariable Long id,
            @RequestBody BeneficioDTO dto) {
        BeneficioEntity b = service.update(id, dto);
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover benefício", description = "Remove um benefício do sistema")
    @ApiResponse(responseCode = "204", description = "Benefício removido com sucesso")
    @ApiResponse(responseCode = "404", description = "Benefício não encontrado")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do benefício") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transfer/{origemId}/{destinoId}/{valor}")
    @Operation(summary = "Transferir valor", description = "Transfere valor entre dois benefícios")
    @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro na transferência (saldo insuficiente, IDs inválidos, etc.)")
    public ResponseEntity<Void> transfer(
            @Parameter(description = "ID do benefício de origem") @PathVariable("origemId") Long origemId,
            @Parameter(description = "ID do benefício de destino") @PathVariable("destinoId") Long destinoId,
            @Parameter(description = "Valor a ser transferido") @PathVariable("valor") Double valor) {
        service.transfer(origemId, destinoId, valor);
        return ResponseEntity.ok().build();
    }
}
