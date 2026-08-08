package com.MyProject.DigitalBankingSystem.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.MyProject.DigitalBankingSystem.account.entity.AccountStatus;
import com.MyProject.DigitalBankingSystem.account.entity.AccountType;

import com.MyProject.DigitalBankingSystem.audit.entity.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse implements Serializable, AuditableEntity {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private AccountStatus status;
    private AccountType accountType;
}

/* Serializable is a Java marker interface that tells Java:

"This object can be converted into a stream of bytes and later reconstructed."

Redis caching often needs this because Spring may serialize your object before storing it in Redis.
*/ 
