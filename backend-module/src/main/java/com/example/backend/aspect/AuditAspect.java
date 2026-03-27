package com.example.backend.aspect;

import com.example.backend.annotation.Audited;
import com.example.backend.entity.AuditLogEntity;
import com.example.backend.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final AuditLogRepository auditLogRepository;

    public AuditAspect(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Around("@annotation(audited)")
    public Object audit(ProceedingJoinPoint joinPoint, Audited audited) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
            saveAuditLog(joinPoint, audited, true, null);
        } catch (Exception e) {
            saveAuditLog(joinPoint, audited, false, e.getMessage());
            throw e;
        }
        return result;
    }

    private void saveAuditLog(ProceedingJoinPoint joinPoint, Audited audited, boolean success, String error) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth != null ? auth.getName() : "anonymous";

            String action = audited.action().isEmpty()
                    ? joinPoint.getSignature().getName()
                    : audited.action();

            String ipAddress = getClientIp();
            String details = success ? "SUCCESS" : "FAILED: " + error;

            AuditLogEntity auditLog = new AuditLogEntity(username, action, audited.entity(), null, details, ipAddress);
            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage());
        }
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception ignored) {}
        return "unknown";
    }
}
