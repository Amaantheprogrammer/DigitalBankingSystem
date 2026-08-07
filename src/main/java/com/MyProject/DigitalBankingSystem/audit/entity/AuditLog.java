package com.MyProject.DigitalBankingSystem.audit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "audit_logs")
@Entity
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private String action;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    private Long entityId;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private AuditLogStatus status;

}
