package com.example.backend.mapper;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.dto.BeneficioListDTO;
import com.example.backend.entity.BeneficioEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class BeneficioMapper {

    public BeneficioEntity toEntity(BeneficioDTO dto) {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setTitular(dto.getNome());
        entity.setSaldo(dto.getSaldo() != null
                ? BigDecimal.valueOf(dto.getSaldo()).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        entity.setAtiva(dto.getAtiva() != null ? dto.getAtiva() : true);
        return entity;
    }

    public void updateEntity(BeneficioEntity entity, BeneficioDTO dto) {
        if (dto.getNome() != null) {
            entity.setTitular(dto.getNome());
        }
        if (dto.getSaldo() != null) {
            entity.setSaldo(BigDecimal.valueOf(dto.getSaldo()).setScale(2, RoundingMode.HALF_UP));
        }
        if (dto.getAtiva() != null) {
            entity.setAtiva(dto.getAtiva());
        }
    }

    public BeneficioDTO toDTO(BeneficioEntity entity) {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getTitular());
        dto.setSaldo(entity.getSaldo() != null ? entity.getSaldo().doubleValue() : 0.0);
        dto.setAtiva(entity.getAtiva());
        return dto;
    }

    public BeneficioListDTO toListDTO(BeneficioEntity entity) {
        BeneficioListDTO dto = new BeneficioListDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getTitular());
        dto.setValor(entity.getSaldo() != null ? entity.getSaldo().doubleValue() : 0.0);
        return dto;
    }
}
