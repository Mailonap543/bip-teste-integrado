package com.example.backend;

import com.example.backend.model.Beneficio;
import com.example.backend.service.BeneficioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Beneficio> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Beneficio buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Beneficio> criar(@RequestBody Beneficio b) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(b));
    }

    @PutMapping("/{id}")
    public Beneficio atualizar(@PathVariable Long id, @RequestBody Beneficio b) {
        return service.atualizar(id, b);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}