package com.MyProject.DigitalBankingSystem.transaction.controller;

import com.MyProject.DigitalBankingSystem.transaction.dto.DepositRequest;
import com.MyProject.DigitalBankingSystem.transaction.dto.TransactionRequest;
import com.MyProject.DigitalBankingSystem.transaction.dto.TransactionResponse;
import com.MyProject.DigitalBankingSystem.transaction.dto.WithdrawRequest;
import com.MyProject.DigitalBankingSystem.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Transactions", description = "Transaction APIs")
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Get transaction by ID",
            description = "Return transaction details by ID"
    )
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long transactionId) {
        log.info("Fetching transaction with ID {}", transactionId);
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));
    }

    @Operation(
            summary = "Get transaction by reference",
            description = "Return transaction details by reference"
    )
    @GetMapping("/reference/{transactionReference}")
    public ResponseEntity<TransactionResponse> getTransactionByReference(@PathVariable String transactionReference) {
        log.info("Fetching transaction with reference {}", transactionReference);
        return ResponseEntity.ok(transactionService.getTransactionByReference(transactionReference));
    }

    @Operation(
            summary = "Get all transactions by account-number",
            description = "Return paginated recent transactions by account-number"
    )
    @GetMapping("/all-transactions/{accountNumber}")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByAccountNumber(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("Fetching all transactions with account number {}", accountNumber);
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber, pageable));
    }

    @Operation(
            summary = "Get user's own transaction by reference",
            description = "Return user's own transaction by reference"
    )
    @GetMapping("/my-transaction/{transactionReference}")
    public ResponseEntity<TransactionResponse> getMyTransaction(@PathVariable String transactionReference) {
        log.info("Fetching your transaction details with reference number {}", transactionReference);
        return ResponseEntity.ok(transactionService.getMyTransaction(transactionReference));
    }

    @Operation(
            summary = "Get user's own transactions by account-number",
            description = "Return authenticated user's own transactions by account-number"
    )
    @GetMapping("/my-transactions/{accountNumber}")
    public ResponseEntity<Page<TransactionResponse>> getMyTransactions(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        log.info("Fetching all transactions with account number {}", accountNumber);
        return ResponseEntity.ok(transactionService.getMyTransactions(accountNumber, pageable));
    }

    @Operation(
            summary = "Transfer amount",
            description = "Transfer amount by taking sender's and receiver's account-number along with the amount"
    )
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody TransactionRequest transactionRequest
    ) {
        log.info("Initiating transfer between accounts");
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.transfer(key, transactionRequest));
    }

    @Operation(
            summary = "Deposit amount",
            description = "Deposit amount by taking user's account-number along with the amount"
    )
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest depositRequest) {
        log.info("Depositing {} into your account", depositRequest.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.deposit(depositRequest));
    }

    @Operation(
            summary = "Withdraw amount",
            description = "Withdraw amount by taking user's account-number along with the amount as"
    )
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {
        log.info("Withdrawing {} from your account", withdrawRequest.getAmount());
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.withdraw(withdrawRequest));
    }
}
