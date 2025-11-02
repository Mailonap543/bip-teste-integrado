package com.example.backend;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.service.BeneficioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    // CORREÇÃO: Tipo de retorno corrigido para BeneficioEntity
    public ResponseEntity<BeneficioEntity> get(@PathVariable Long id) {

        // CORREÇÃO: Variável tipada corretamente e casting removido
        BeneficioEntity b = service.get(id);

        return b == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(b);
    }


    @PostMapping
    // CORREÇÃO: Tipo de retorno corrigido para BeneficioEntity
    public ResponseEntity<BeneficioEntity> create(@RequestBody BeneficioDTO dto) {

        // CORREÇÃO: Variável tipada corretamente e casting removido
        BeneficioEntity b = service.create(dto);

        return ResponseEntity.ok(b);
    }


    @PutMapping("/{id}")
    // CORREÇÃO: Tipo de retorno corrigido para BeneficioEntity
    public ResponseEntity<BeneficioEntity> update(@PathVariable Long id, @RequestBody BeneficioDTO dto) {
        try {
            // CORREÇÃO: Variável tipada corretamente e casting removido
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
}