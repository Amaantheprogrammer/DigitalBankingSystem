package com.MyProject.DigitalBankingSystem.fraud.dto;

import com.MyProject.DigitalBankingSystem.fraud.entity.FraudStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudLogRequest {
    @NotNull(message = "Fraud status cannot be blank")
    private FraudStatus fraudStatus;
}
