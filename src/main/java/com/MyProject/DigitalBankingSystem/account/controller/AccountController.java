package com.MyProject.DigitalBankingSystem.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.MyProject.DigitalBankingSystem.account.dto.AccountRequest;
import com.MyProject.DigitalBankingSystem.account.dto.AccountResponse;
import com.MyProject.DigitalBankingSystem.account.dto.UpdateAccountRequest;
import com.MyProject.DigitalBankingSystem.account.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) { // Admin
        log.info("Fetching account with ID {}", accountId);
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<AccountResponse> getByAccountNumber(@PathVariable String accountNumber) { // Admin
        log.info("Fetching account with account number {}", accountNumber);
        return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(@PathVariable Long userId) { // Admin
        log.info("Fetching all accounts with user ID {}", userId);
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/my-account/{accountNumber}")
    public ResponseEntity<AccountResponse> getMyAccount(@PathVariable String accountNumber) { // User + Admin
        return ResponseEntity.ok(accountService.getMyAccount(accountNumber));
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts() { // User + Admin
        return ResponseEntity.ok(accountService.getMyAccounts());
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) { // User + Admin
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountRequest));
    }

    @PatchMapping("/status")
    public ResponseEntity<AccountResponse> updateStatus(@Valid @RequestBody UpdateAccountRequest updateAccountRequest) { // Admin
        return ResponseEntity.ok(accountService.updateStatus(updateAccountRequest));
    }

}
