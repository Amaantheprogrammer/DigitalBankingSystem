package com.MyProject.DigitalBankingSystem.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.MyProject.DigitalBankingSystem.account.entity.AccountStatus;
import com.MyProject.DigitalBankingSystem.account.entity.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long id;
    private Long accountNumber;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private AccountStatus status;
    private AccountType accountType;
}