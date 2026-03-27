package com.example.backend.service;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.entity.BeneficioEntity;
import com.example.backend.entity.TransferenceEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.mapper.BeneficioMapper;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BeneficioService {

    private static final Logger log = LoggerFactory.getLogger(BeneficioService.class);

    private final BeneficioRepository beneficioRepository;
    private final TransferenceRepository transferenceRepository;
    private final BeneficioMapper beneficioMapper;

    public BeneficioService(BeneficioRepository beneficioRepository,
                            TransferenceRepository transferenceRepository,
                            BeneficioMapper beneficioMapper) {
        this.beneficioRepository = beneficioRepository;
        this.transferenceRepository = transferenceRepository;
        this.beneficioMapper = beneficioMapper;
    }

    @Transactional
    @CacheEvict(value = "beneficios", allEntries = true)
    public BeneficioEntity create(BeneficioDTO dto) {
        log.info("Creating beneficio for titular: {}", dto.getNome());
        BeneficioEntity entity = beneficioMapper.toEntity(dto);
        BeneficioEntity saved = beneficioRepository.save(entity);
        log.info("Beneficio created with id: {}", saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "beneficios", key = "'all'")
    public List<BeneficioEntity> listAll() {
        log.debug("Listing all beneficios");
        return beneficioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<BeneficioEntity> listPaged(Pageable pageable) {
        log.debug("Listing beneficios paged: {}", pageable);
        return beneficioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<BeneficioEntity> findByTitular(String titular, Pageable pageable) {
        log.debug("Finding beneficios by titular: {}", titular);
        return beneficioRepository.findByTitularContainingIgnoreCase(titular, pageable);
    }

    @Transactional(readOnly = true)
    public Page<BeneficioEntity> findByAtiva(Boolean ativa, Pageable pageable) {
        log.debug("Finding beneficios by ativa: {}", ativa);
        return beneficioRepository.findByAtiva(ativa, pageable);
    }

    @Transactional
    @CacheEvict(value = "beneficios", allEntries = true)
    public BeneficioEntity update(Long id, BeneficioDTO dto) {
        log.info("Updating beneficio id: {}", id);
        BeneficioEntity entity = findByIdOrThrow(id);
        beneficioMapper.updateEntity(entity, dto);
        return beneficioRepository.save(entity);
    }

    @Transactional
    @CacheEvict(value = "beneficios", allEntries = true)
    public void delete(Long id) {
        log.info("Deleting beneficio id: {}", id);
        BeneficioEntity entity = findByIdOrThrow(id);
        beneficioRepository.deleteById(entity.getId());
        log.info("Beneficio deleted id: {}", id);
    }

    @Transactional
    @CacheEvict(value = "beneficios", allEntries = true)
    public void transfer(Long fromId, Long toId, Double amount) {
        log.info("Transfer: from {} to {} amount {}", fromId, toId, amount);

        if (fromId == null || toId == null)
            throw new BusinessException("IDs devem ser informados");
        if (fromId.equals(toId))
            throw new BusinessException("Origem e destino iguais");
        if (amount == null || amount <= 0)
            throw new BusinessException("Valor invalido para transferencia");

        BeneficioEntity origem = findByIdOrThrow(fromId);
        BeneficioEntity destino = findByIdOrThrow(toId);

        if (Boolean.FALSE.equals(origem.getAtiva()) || Boolean.FALSE.equals(destino.getAtiva()))
            throw new BusinessException("Beneficio(s) inativo(s)");

        BigDecimal valor = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);

        if (origem.getSaldo().compareTo(valor) < 0) {
            log.warn("Insufficient balance: available {} requested {}", origem.getSaldo(), valor);
            throw new BusinessException("Saldo insuficiente no beneficio de origem.");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor).setScale(2, RoundingMode.HALF_UP));
        destino.setSaldo(destino.getSaldo().add(valor).setScale(2, RoundingMode.HALF_UP));

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        TransferenceEntity transferencia = new TransferenceEntity(origem.getId(), destino.getId(), valor);
        transferenceRepository.save(transferencia);

        log.info("Transfer completed: from {} to {} amount {}", fromId, toId, amount);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "beneficios", key = "#id")
    public BeneficioEntity get(Long id) {
        return findByIdOrThrow(id);
    }

    private BeneficioEntity findByIdOrThrow(Long id) {
        return beneficioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Beneficio nao encontrado: " + id));
    }
}
