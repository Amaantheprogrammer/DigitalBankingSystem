package com.MyProject.DigitalBankingSystem.audit.aspect;

import java.time.LocalDateTime;

import com.MyProject.DigitalBankingSystem.audit.entity.AuditableEntity;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.MyProject.DigitalBankingSystem.audit.annotation.Auditable;
import com.MyProject.DigitalBankingSystem.audit.entity.AuditLog;
import com.MyProject.DigitalBankingSystem.audit.entity.AuditLogStatus;
import com.MyProject.DigitalBankingSystem.audit.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logSuccess(Auditable auditable, Object result) {
        Long entityId = null;
        if (result instanceof AuditableEntity entity) {
            entityId = entity.getId();
        }
        AuditLog log = AuditLog.builder()
                .userEmail(getCurrentUserEmail())
                .action(auditable.action())
                .entityType(auditable.entityType())
                .entityId(entityId)
                .timestamp(LocalDateTime.now())
                .status(AuditLogStatus.SUCCESS)
                .build();
        auditLogRepository.save(log);
    }

    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "ex")
    public void logFailure(Auditable auditable, Exception ex) {
        AuditLog log = AuditLog.builder()
                .userEmail(getCurrentUserEmail())
                .action(auditable.action())
                .timestamp(LocalDateTime.now())
                .status(AuditLogStatus.FAILURE)
                .build();
        auditLogRepository.save(log);
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "NULL";
    }
}
