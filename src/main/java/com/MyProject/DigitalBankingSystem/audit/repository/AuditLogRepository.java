package com.MyProject.DigitalBankingSystem.audit.repository;

import com.MyProject.DigitalBankingSystem.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
