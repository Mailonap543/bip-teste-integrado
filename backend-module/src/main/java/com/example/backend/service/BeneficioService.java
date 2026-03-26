package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.entity.TransferenceEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BeneficioService {

    private static final Logger log = LoggerFactory.getLogger(BeneficioService.class);

    private final BeneficioRepository beneficioRepository;
    private final TransferenceRepository transferenceRepository;

    public BeneficioService(BeneficioRepository beneficioRepository,
                            TransferenceRepository transferenceRepository) {
        this.beneficioRepository = beneficioRepository;
        this.transferenceRepository = transferenceRepository;
    }

    public BeneficioEntity create(BeneficioDTO dto) {
        log.info("Creating beneficio for titular: {}", dto.getNome());
        BeneficioEntity entity = new BeneficioEntity();
        entity.setTitular(dto.getNome());

        BigDecimal saldo = dto.getSaldo() != null
                ? BigDecimal.valueOf(dto.getSaldo()).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        entity.setSaldo(saldo);
        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);

        BeneficioEntity saved = beneficioRepository.save(entity);
        log.info("Beneficio created with id: {}", saved.getId());
        return saved;
    }

    public List<BeneficioEntity> listAll() {
        log.debug("Listing all beneficios");
        return beneficioRepository.findAll();
    }

    @Transactional
    public BeneficioEntity update(Long id, BeneficioDTO dto) {
        log.info("Updating beneficio id: {}", id);
        BeneficioEntity entity = findByIdOrThrow(id);

        if (dto.getNome() != null) {
            entity.setTitular(dto.getNome());
        }
        if (dto.getSaldo() != null) {
            entity.setSaldo(BigDecimal.valueOf(dto.getSaldo()).setScale(2, RoundingMode.HALF_UP));
        }
        if (dto.getAtiva() != null) {
            entity.setAtiva(dto.getAtiva());
        }

        return beneficioRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting beneficio id: {}", id);
        BeneficioEntity entity = findByIdOrThrow(id);
        beneficioRepository.deleteById(entity.getId());
        log.info("Beneficio deleted id: {}", id);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, Double amount) {
        log.info("Transfer: from {} to {} amount {}", fromId, toId, amount);

        if (fromId == null || toId == null)
            throw new BusinessException("IDs devem ser informados");
        if (fromId.equals(toId))
            throw new BusinessException("Origem e destino iguais");
        if (amount == null || amount <= 0)
            throw new BusinessException("Valor inválido para transferência");

        BeneficioEntity origem = findByIdOrThrow(fromId);
        BeneficioEntity destino = findByIdOrThrow(toId);

        if (Boolean.FALSE.equals(origem.getAtiva()) || Boolean.FALSE.equals(destino.getAtiva()))
            throw new BusinessException("Benefício(s) inativo(s)");

        BigDecimal valor = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);

        if (origem.getSaldo().compareTo(valor) < 0) {
            log.warn("Insufficient balance: available {} requested {}", origem.getSaldo(), valor);
            throw new BusinessException("Saldo insuficiente no benefício de origem.");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor).setScale(2, RoundingMode.HALF_UP));
        destino.setSaldo(destino.getSaldo().add(valor).setScale(2, RoundingMode.HALF_UP));

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        TransferenceEntity transferencia = new TransferenceEntity(origem.getId(), destino.getId(), valor);
        transferenceRepository.save(transferencia);

        log.info("Transfer completed: from {} to {} amount {}", fromId, toId, amount);
    }

    public BeneficioEntity get(Long id) {
        return findByIdOrThrow(id);
    }

    private BeneficioEntity findByIdOrThrow(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Benefício não encontrado: " + id));
    }
}
