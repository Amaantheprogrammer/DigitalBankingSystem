package com.MyProject.DigitalBankingSystem.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    @NotBlank(message = "Sender account number is required")
    private String senderAccountNumber;
    @NotBlank(message = "Receiver account number is required")
    private String receiverAccountNumber;
    @NotBlank(message = "Amount is required")
    private BigDecimal amount;
}