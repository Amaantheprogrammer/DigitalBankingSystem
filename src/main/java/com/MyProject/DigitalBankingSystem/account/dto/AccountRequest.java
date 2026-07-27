package com.MyProject.DigitalBankingSystem.account.dto;

import com.MyProject.DigitalBankingSystem.account.entity.AccountType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @NotNull(message = "Account type is a required field")
    private AccountType accountType;
}