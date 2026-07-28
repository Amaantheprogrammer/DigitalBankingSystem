package com.MyProject.DigitalBankingSystem.transaction.controller;

import com.MyProject.DigitalBankingSystem.transaction.dto.DepositRequest;
import com.MyProject.DigitalBankingSystem.transaction.dto.TransactionRequest;
import com.MyProject.DigitalBankingSystem.transaction.dto.TransactionResponse;
import com.MyProject.DigitalBankingSystem.transaction.dto.WithdrawRequest;
import com.MyProject.DigitalBankingSystem.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        log.info("Fetching transaction with ID: {}", transactionId);
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @GetMapping("/reference/{transactionReference}")
    public ResponseEntity<TransactionResponse> getTransactionByReference(@PathVariable String transactionReference) {
        log.info("Fetching transaction with reference: {}", transactionReference);
        return ResponseEntity.ok(transactionService.getTransactionByReference(transactionReference));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByAccountNumber(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber, pageable));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransactionRequest transactionRequest) {
        log.info("Initiating transfer between accounts");
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(transactionRequest));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest depositRequest) {
        log.info("Depositing {} into account", depositRequest.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.deposit(depositRequest));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        log.info("Withdrawing {} from account", withdrawRequest.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.withdraw(withdrawRequest));
    }
}
