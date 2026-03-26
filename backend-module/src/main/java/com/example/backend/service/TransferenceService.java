package com.example.backend.service;

import com.example.backend.entity.BeneficioEntity;
import com.example.backend.entity.TransferenceEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferenceService {

    private static final Logger log = LoggerFactory.getLogger(TransferenceService.class);

    private final BeneficioRepository beneficioRepository;
    private final TransferenceRepository transferenceRepository;

    public TransferenceService(BeneficioRepository beneficioRepository,
                               TransferenceRepository transferenceRepository) {
        this.beneficioRepository = beneficioRepository;
        this.transferenceRepository = transferenceRepository;
    }

    @Transactional
    public void transferir(Long fromId, Long toId, BigDecimal valor) {
        log.info("Transferencia: from {} to {} valor {}", fromId, toId, valor);

        if (fromId == null || toId == null) {
            throw new BusinessException("IDs de origem e destino são obrigatórios");
        }
        if (fromId.equals(toId)) {
            throw new BusinessException("Não é possível transferir para o mesmo benefício");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor da transferência deve ser positivo");
        }

        valor = valor.setScale(2, RoundingMode.HALF_UP);

        BeneficioEntity origem = beneficioRepository.findById(fromId)
                .orElseThrow(() -> new BusinessException("Benefício de origem não encontrado"));
        BeneficioEntity destino = beneficioRepository.findById(toId)
                .orElseThrow(() -> new BusinessException("Benefício de destino não encontrado"));

        if (!origem.getAtiva() || !destino.getAtiva()) {
            throw new BusinessException("Ambos os benefícios devem estar ativos");
        }

        if (origem.getSaldo().compareTo(valor) < 0) {
            log.warn("Saldo insuficiente: disponivel {} solicitado {}", origem.getSaldo(), valor);
            throw new BusinessException("Saldo insuficiente para transferência");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor).setScale(2, RoundingMode.HALF_UP));
        destino.setSaldo(destino.getSaldo().add(valor).setScale(2, RoundingMode.HALF_UP));

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        TransferenceEntity t = new TransferenceEntity(fromId, toId, valor);
        transferenceRepository.save(t);

        log.info("Transferencia concluida: from {} to {} valor {}", fromId, toId, valor);
    }
}
