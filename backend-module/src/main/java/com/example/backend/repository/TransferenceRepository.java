package com.example.backend.repository;


import com.example.backend.entity.TransferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenceRepository extends JpaRepository<TransferenceEntity, Long> {


    List<TransferenceEntity> findByContaOrigemId(Long contaOrigemId);


    List<TransferenceEntity> findByContaDestinoId(Long contaDestinoId);
}
