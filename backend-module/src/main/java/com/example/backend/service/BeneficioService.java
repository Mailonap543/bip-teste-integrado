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

        BigDecimal saldo = dto.getSaldo() != null
                ? BigDecimal.valueOf(dto.getSaldo()).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        entity.setSaldo(saldo);

        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);

        return beneficioRepository.save(entity);
    }

    public List<BeneficioEntity> listAll() {
        return beneficioRepository.findAll();
    }

    @Transactional
    public BeneficioEntity update(Long id, BeneficioDTO dto) {
        BeneficioEntity entity = findByIdOrThrow(id);

        if (dto.getNome() != null) {
            entity.setTitular(dto.getNome());
        }

        if (dto.getSaldo() != null) {
            BigDecimal saldo = BigDecimal.valueOf(dto.getSaldo()).setScale(2, RoundingMode.HALF_UP);
            entity.setSaldo(saldo);
        }

        if (dto.getAtiva() != null) {
            entity.setAtiva(dto.getAtiva());
        }

        return beneficioRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
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

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente no benefício de origem.");
        }

        origem.setSaldo(round(origem.getSaldo().subtract(valor)));
        destino.setSaldo(round(destino.getSaldo().add(valor)));

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        TransferenceEntity transferencia = new TransferenceEntity(origem.getId(), destino.getId(), valor);
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
