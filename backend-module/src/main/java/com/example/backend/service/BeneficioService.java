package com.example.backend.service;

import com.example.backend.model.Beneficio;
import com.example.backend.repository.BeneficioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;

    public BeneficioService(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    public List<Beneficio> listar() {
        return beneficioRepository.findAll();
    }

    public Beneficio buscar(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Benefício não encontrado: " + id));
    }


    public Beneficio criar(Beneficio b) {
        return beneficioRepository.save(b);
    }


    public Beneficio atualizar(Long id, Beneficio novosDados) {
        Beneficio beneficio = buscar(id);

        beneficio.setNome(novosDados.getNome());
        beneficio.setDescricao(novosDados.getDescricao());
        beneficio.setValor(novosDados.getValor());
        beneficio.setAtivo(novosDados.getAtivo());

        return beneficioRepository.save(beneficio);
    }


    public void deletar(Long id) {
        if (!beneficioRepository.existsById(id))
            throw new RuntimeException("Benefício não encontrado: " + id);

        beneficioRepository.deleteById(id);
    }
}