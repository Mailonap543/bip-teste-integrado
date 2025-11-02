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

import static java.math.BigDecimal.valueOf;

@Service
public class BeneficioService {

    private final BeneficioRepository beneficioRepository;
    private final TransferenceRepository transferenceRepository;

    public BeneficioService(BeneficioRepository beneficioRepository, TransferenceRepository transferenceRepository) {
        this.beneficioRepository = beneficioRepository;
        this.transferenceRepository = transferenceRepository;
    }

    /**
     * Cria um novo Benefício a partir de um DTO.
     */
    // CORREÇÃO: Tipo de retorno deve ser apenas BeneficioEntity
    public BeneficioEntity create(BeneficioDTO dto) {
        BeneficioEntity entity = new BeneficioEntity();

        entity.setTitular(dto.getNome());

        // Corrigido para verificar se o valor é diferente de 0.0 (se o DTO for double primitivo)
        entity.setValor(dto.getValor() != 0.0
                ? valueOf(dto.getValor())
                : BigDecimal.ZERO);

        // Corrigido para verificar se é null (se o DTO for Boolean Wrapper)
        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);

        // CORREÇÃO: Adicionando o retorno obrigatório
        return (BeneficioEntity) beneficioRepository.save(entity);
    }

    /**
     * Lista todos os Benefícios.
     */
    public List<BeneficioEntity> listAll() {
        // CORREÇÃO: Deve chamar o método do repositório
        return beneficioRepository.findAll();
    }

    /**
     * Atualiza um Benefício existente.
     */
    // CORREÇÃO: Tipo de retorno deve ser apenas BeneficioEntity
    public BeneficioEntity update(Long id, BeneficioDTO dto) {
        // CORREÇÃO: findByIdOrThrow já retorna o tipo correto
        BeneficioEntity entity = findByIdOrThrow(id);

        entity.setTitular(dto.getNome());

        entity.setValor(dto.getValor() != 0.0
                ? valueOf(dto.getValor())
                : BigDecimal.ZERO);

        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);

        return (BeneficioEntity) beneficioRepository.save(entity);
    }

    /**
     * Deleta um Benefício pelo ID.
     */
    public void delete(Long id) {
        // CORREÇÃO: Acesso via instância do repositório
        if (!beneficioRepository.existsById(id)) {
            throw new BusinessException("Benefício não encontrado: " + id);
        }
        beneficioRepository.deleteById(id);
    }

    /**
     * Realiza a transferência de valor entre dois Benefícios.
     */
    @Transactional
    public void transfer(Long fromId, Long toId, Double amount) {
        if (fromId == null || toId == null)
            throw new BusinessException("IDs devem ser informados");
        if (fromId.equals(toId))
            throw new BusinessException("Origem e destino iguais");
        if (amount == null || amount.doubleValue() <= 0)
            throw new BusinessException("Valor inválido para transferência");

        BeneficioEntity origem = findByIdOrThrow(fromId);
        BeneficioEntity destino = findByIdOrThrow(toId);

        if (Boolean.FALSE.equals(origem.getAtiva()) || Boolean.FALSE.equals(destino.getAtiva()))
            throw new BusinessException("Benefício(s) inativo(s). A transferência não pode ser realizada.");

        BigDecimal valor = valueOf(amount);

        if (origem.getValor().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente no benefício de origem.");
        }

        origem.setValor(round(origem.getValor().subtract(valor)));
        destino.setValor(round(destino.getValor().add(valor)));

        beneficioRepository.save(origem);
        beneficioRepository.save(destino);

        TransferenceEntity transferencia = new TransferenceEntity(origem.getId(), destino.getId(), amount);
        // CORREÇÃO: Acesso via instância do repositório de transferência
        transferenceRepository.save(transferencia);
    }

    /**
     * Arredonda o valor BigDecimal.
     */
    private BigDecimal round(BigDecimal val) {
        return val.setScale(2, RoundingMode.HALF_UP);
    }

    public BeneficioEntity get(Long id) {

        return findByIdOrThrow(id);
    }
    private BeneficioEntity findByIdOrThrow(Long id) {
        // CORREÇÃO: Retorna o resultado da busca
        return (BeneficioEntity) beneficioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Benefício não encontrado: " + id));
    }
}