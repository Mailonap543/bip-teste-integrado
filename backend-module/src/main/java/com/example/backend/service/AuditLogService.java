package com.example.backend.service;

import com.example.backend.entity.AuditLogEntity;
import com.example.backend.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private static final Logger log = LoggerFactory.getLogger(AuditLogService.class);

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public Page<AuditLogEntity> findAll(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    public Page<AuditLogEntity> findByUsername(String username, Pageable pageable) {
        return auditLogRepository.findByUsername(username, pageable);
    }

    public Page<AuditLogEntity> findByAction(String action, Pageable pageable) {
        return auditLogRepository.findByAction(action, pageable);
    }

    public Page<AuditLogEntity> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return auditLogRepository.findByTimestampBetween(start, end, pageable);
    }

    public Page<AuditLogEntity> findByEntity(String entityName, Long entityId, Pageable pageable) {
        if (entityId != null) {
            return auditLogRepository.findByEntityNameAndEntityId(entityName, entityId, pageable);
        }
        return auditLogRepository.findByEntityName(entityName, pageable);
    }
}
