package com.example.backend.service;

import com.example.backend.entity.BeneficioEntity;
import com.example.backend.entity.TransferenceEntity;
import com.example.backend.exception.BusinessException;
import com.example.backend.repository.BeneficioRepository;
import com.example.backend.repository.TransferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferenceService {

    private final BeneficioRepository beneficioRepository;
    private final TransferenceRepository transferenceRepository;

    public TransferenceService(BeneficioRepository beneficioRepository,
                               TransferenceRepository transferenceRepository) {
        this.beneficioRepository = beneficioRepository;
        this.transferenceRepository = transferenceRepository;
    }


    @Transactional
    public void transferir(Long fromId, Long toId, BigDecimal valor) {
        if (fromId.equals(toId)) {
            throw new BusinessException("Não é possível transferir para o mesmo benefício");
        }

        BeneficioEntity origem = beneficioRepository.findById(fromId)
                .orElseThrow(() -> new BusinessException("Benefício de origem não encontrado"));

        BeneficioEntity destino = beneficioRepository.findById(toId)
                .orElseThrow(() -> new BusinessException("Benefício de destino não encontrado"));

        if (!origem.getAtiva() || !destino.getAtiva()) {
            throw new BusinessException("Ambos os benefícios devem estar ativos");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O valor da transferência deve ser positivo");
        }

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente para transferência");
        }

        // Atualiza saldos
        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        // Salva no banco (lock é tratado pelo @Transactional e JPA)
        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        TransferenceEntity t = null;
        transferenceRepository.save(t);
    }
}
