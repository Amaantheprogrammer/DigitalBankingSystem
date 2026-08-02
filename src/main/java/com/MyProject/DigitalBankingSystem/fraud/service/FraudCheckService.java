package com.MyProject.DigitalBankingSystem.fraud.service;

import com.MyProject.DigitalBankingSystem.account.entity.Account;
import com.MyProject.DigitalBankingSystem.exception.FraudDetectionException;
import com.MyProject.DigitalBankingSystem.fraud.entity.FraudLog;
import com.MyProject.DigitalBankingSystem.fraud.entity.FraudStatus;
import com.MyProject.DigitalBankingSystem.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudCheckService {

    private final FraudLogService fraudLogService;
    private final TransactionRepository transactionRepository;
    private static final BigDecimal TRANSACTION_LIMIT = new BigDecimal("50000");
    private static final long MAX_TRANSACTIONS_PER_MINUTE = 5;

    @Transactional
    public void checkTransactionLimit(Account suspiciousAccount, BigDecimal amount, String transactionReference) {
        if (amount.compareTo(TRANSACTION_LIMIT) > 0) {
            FraudLog fraudLog = FraudLog.builder()
                    .accountNumber(suspiciousAccount.getAccountNumber())
                    .transactionReference(transactionReference)
                    .status(FraudStatus.SUSPICIOUS)
                    .reason("Transaction amount exceeds threshold of " + TRANSACTION_LIMIT)
                    .detectedAt(LocalDateTime.now())
                    .build();
            fraudLogService.saveFraudLog(fraudLog);
        }
    }

    @Transactional
    public void checkTransactionFrequency(Account suspiciousAccount, String transactionReference) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);

        long transactionCount = transactionRepository.countBySenderAccountAndTransactionAtAfter(
                suspiciousAccount,
                oneMinuteAgo
        );

        if (transactionCount >= MAX_TRANSACTIONS_PER_MINUTE) {
            FraudLog fraudLog = FraudLog.builder()
                    .accountNumber(suspiciousAccount.getAccountNumber())
                    .transactionReference(transactionReference)
                    .status(FraudStatus.BLOCKED)
                    .reason("Transaction frequency exceeds the threshold of " + MAX_TRANSACTIONS_PER_MINUTE)
                    .detectedAt(LocalDateTime.now())
                    .build();
            fraudLogService.saveFraudLog(fraudLog);
            throw new FraudDetectionException("Transaction blocked due to suspicious transfer frequency");
        }
    }
}
