package com.MyProject.DigitalBankingSystem.transaction.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.MyProject.DigitalBankingSystem.audit.entity.AuditableEntity;
import com.MyProject.DigitalBankingSystem.transaction.entity.TransactionStatus;
import com.MyProject.DigitalBankingSystem.transaction.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse implements Serializable, AuditableEntity {
    private Long id;
    private String transactionReference;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime transactionAt;
}