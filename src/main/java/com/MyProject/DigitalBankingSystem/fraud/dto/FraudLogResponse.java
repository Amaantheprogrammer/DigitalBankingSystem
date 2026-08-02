package com.MyProject.DigitalBankingSystem.fraud.dto;

import com.MyProject.DigitalBankingSystem.fraud.entity.FraudStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudLogResponse {
    private Long id;
    private String accountNumber;
    private String transactionReference;
    private FraudStatus status;
    private String reason;
    private LocalDateTime detectedAt;
    private LocalDateTime reviewedAt;
    private String reviewedByEmail;
}
