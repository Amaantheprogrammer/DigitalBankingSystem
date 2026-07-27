package com.MyProject.DigitalBankingSystem.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<AccountResponse> getByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<AccountResponse> createAccount(
            @PathVariable Long userId,
            @Valid @RequestBody AccountRequest accountRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountRequest, userId));
    }

    @PatchMapping("/status/{accountId}")
    public ResponseEntity<AccountResponse> updateStatus(
            @Valid @RequestBody UpdateAccountRequest updateAccountRequest,
            @PathVariable Long accountId
    ) {
        return ResponseEntity.ok(accountService.updateStatus(accountId, updateAccountRequest));
    }
}
