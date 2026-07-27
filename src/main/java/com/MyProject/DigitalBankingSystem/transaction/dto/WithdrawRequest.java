package com.MyProject.DigitalBankingSystem.transaction.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}