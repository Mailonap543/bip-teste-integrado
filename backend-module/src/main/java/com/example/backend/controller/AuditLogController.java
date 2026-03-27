package com.example.backend.controller;

import com.example.backend.dto.ApiResponseDTO;
import com.example.backend.dto.PagedResponse;
import com.example.backend.entity.AuditLogEntity;
import com.example.backend.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.example.backend.constants.ApiConstants.MAX_PAGE_SIZE;

@RestController
@RequestMapping("/api/v1/audit")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Auditoria", description = "Endpoints de consulta de logs de auditoria")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @Operation(summary = "Listar logs de auditoria")
    public ResponseEntity<ApiResponseDTO<PagedResponse<AuditLogEntity>>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("timestamp").descending());
        PagedResponse<AuditLogEntity> response = PagedResponse.from(auditLogService.findAll(pageable));
        return ResponseEntity.ok(ApiResponseDTO.ok("Logs listados com sucesso", response));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Listar logs por usuário")
    public ResponseEntity<ApiResponseDTO<PagedResponse<AuditLogEntity>>> findByUser(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("timestamp").descending());
        PagedResponse<AuditLogEntity> response = PagedResponse.from(auditLogService.findByUsername(username, pageable));
        return ResponseEntity.ok(ApiResponseDTO.ok("Logs listados com sucesso", response));
    }

    @GetMapping("/action/{action}")
    @Operation(summary = "Listar logs por ação")
    public ResponseEntity<ApiResponseDTO<PagedResponse<AuditLogEntity>>> findByAction(
            @PathVariable String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("timestamp").descending());
        PagedResponse<AuditLogEntity> response = PagedResponse.from(auditLogService.findByAction(action, pageable));
        return ResponseEntity.ok(ApiResponseDTO.ok("Logs listados com sucesso", response));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Listar logs por intervalo de datas")
    public ResponseEntity<ApiResponseDTO<PagedResponse<AuditLogEntity>>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int pageSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("timestamp").descending());
        PagedResponse<AuditLogEntity> response = PagedResponse.from(auditLogService.findByDateRange(start, end, pageable));
        return ResponseEntity.ok(ApiResponseDTO.ok("Logs listados com sucesso", response));
    }
}
