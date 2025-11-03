package com.example.backend.controller;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.service.BeneficioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<BeneficioEntity>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<BeneficioEntity> get(@PathVariable Long id) {
        BeneficioEntity b = service.get(id);
        return b == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(b);
    }


    @PostMapping
    public ResponseEntity<BeneficioEntity> create(@RequestBody BeneficioDTO dto) {
        BeneficioEntity b = service.create(dto);
        return ResponseEntity.ok(b);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BeneficioEntity> update(@PathVariable Long id, @RequestBody BeneficioDTO dto) {
        try {
            BeneficioEntity b = service.update(id, dto);
            return ResponseEntity.ok(b);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/transfer/{origemId}/{destinoId}/{valor}")
    public ResponseEntity<Void> transfer(
            @PathVariable("origemId") Long origemId,
            @PathVariable("destinoId") Long destinoId,
            @PathVariable("valor") Double valor) {
        try {
            service.transfer(origemId, destinoId, valor);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}