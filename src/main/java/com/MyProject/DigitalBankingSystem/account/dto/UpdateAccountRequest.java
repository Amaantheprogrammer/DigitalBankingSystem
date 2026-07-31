package com.MyProject.DigitalBankingSystem.account.dto;

import com.MyProject.DigitalBankingSystem.account.entity.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateAccountRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    @NotBlank(message = "Account status is required")
    private AccountStatus status;
}
