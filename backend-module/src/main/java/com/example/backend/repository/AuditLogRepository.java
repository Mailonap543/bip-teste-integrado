package com.example.backend.repository;

import com.example.backend.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {

    Page<AuditLogEntity> findByUsername(String username, Pageable pageable);

    Page<AuditLogEntity> findByAction(String action, Pageable pageable);

    Page<AuditLogEntity> findByEntityName(String entityName, Pageable pageable);

    Page<AuditLogEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<AuditLogEntity> findByEntityNameAndEntityId(String entityName, Long entityId, Pageable pageable);
}
