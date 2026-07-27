package com.MyProject.DigitalBankingSystem.transaction.dto;

import com.MyProject.DigitalBankingSystem.transaction.entity.TransactionStatus;
import com.MyProject.DigitalBankingSystem.transaction.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String transactionReference;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private LocalDateTime transactionAt;
}