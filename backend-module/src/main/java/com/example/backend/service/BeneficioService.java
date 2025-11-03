package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.entity.TransferenceEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final TransferenceRepository transferenceRepository;

    public BeneficioService(BeneficioRepository beneficioRepository,
                            TransferenceRepository transferenceRepository) {
        this.beneficioRepository = beneficioRepository;
        this.transferenceRepository = transferenceRepository;
    }



    public BeneficioEntity create(BeneficioDTO dto) {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setTitular(dto.getNome());


        BigDecimal valor = dto.getValor() != null
                ? BigDecimal.valueOf(dto.getValor()).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        entity.setValor(valor);


        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);

        return beneficioRepository.save(entity);
    }


    public List<BeneficioEntity> listAll() {
        return beneficioRepository.findAll();
    }

    /**
     * Atualiza um benefício existente
     */
    public BeneficioEntity update(Long id, BeneficioDTO dto) {
        BeneficioEntity entity = findByIdOrThrow(id);

        if (dto.getNome() != null) {
            entity.setTitular(dto.getNome());
        }

        if (dto.getValor() != null) {
            BigDecimal valor = BigDecimal.valueOf(dto.getValor()).setScale(2, RoundingMode.HALF_UP);
            entity.setValor(valor);
        }

        if (dto.getAtiva() != null) {
            entity.setAtiva(dto.getAtiva());
        }

        return beneficioRepository.save(entity);
    }


    public void delete(Long id) {
        // Encontra o benefício ou lança exceção (garantindo que ele exista)
        BeneficioEntity entity = findByIdOrThrow(id);
        beneficioRepository.deleteById(entity.getId());
    }


    @Transactional
    public void transfer(Long fromId, Long toId, Double amount) {
        if (fromId == null || toId == null)
            throw new BusinessException("IDs devem ser informados");
        if (fromId.equals(toId))
            throw new BusinessException("Origem e destino iguais");
        if (amount == null || amount <= 0)
            throw new BusinessException("Valor inválido para transferência");

        BeneficioEntity origem = findByIdOrThrow(fromId);
        BeneficioEntity destino = findByIdOrThrow(toId);

        if (Boolean.FALSE.equals(origem.getAtiva()) || Boolean.FALSE.equals(destino.getAtiva()))
            throw new BusinessException("Benefício(s) inativo(s). A transferência não pode ser realizada.");

        BigDecimal valor = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);

        // Comparação correta de BigDecimal
        if (origem.getValor().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente no benefício de origem.");
        }

        // Realiza o débito e o crédito, garantindo o arredondamento
        origem.setValor(round(origem.getValor().subtract(valor)));
        destino.setValor(round(destino.getValor().add(valor)));

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);


        TransferenceEntity transferencia = new TransferenceEntity(origem.getId(), destino.getId(), amount);
        transferenceRepository.save(transferencia);
    }


    private BigDecimal round(BigDecimal val) {
        return val.setScale(2, RoundingMode.HALF_UP);
    }


    public BeneficioEntity get(Long id) {
        return findByIdOrThrow(id);
    }


    private BeneficioEntity findByIdOrThrow(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Benefício não encontrado: " + id));
    }
}