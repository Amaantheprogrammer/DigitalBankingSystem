package com.MyProject.DigitalBankingSystem.account.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Accounts", description = "Account management APIs")
@RequestMapping("/accounts")
public class AccountController {
    
    private final AccountService accountService;

    @Operation(
            summary = "Get account by ID",
            description = "Returns account details by a given account ID"
    )
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) { // Admin
        log.info("Fetching account with ID {}", accountId);
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @Operation(
            summary = "Get account by account-number",
            description = "Returns account details by a given account-number"
    )
    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<AccountResponse> getByAccountNumber(@PathVariable String accountNumber) { // Admin
        log.info("Fetching account with account number {}", accountNumber);
        return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber));
    }

    @Operation(
            summary = "Get Accounts by user ID",
            description = "Returns all acounts of a user by user ID"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(@PathVariable Long userId) { // Admin
        log.info("Fetching all accounts with user ID {}", userId);
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @Operation(
            summary = "Get account by user's own account number",
            description = "Returns account details of the user which is currently logged in, by the account number"
    )
    @GetMapping("/my-account/{accountNumber}")
    public ResponseEntity<AccountResponse> getMyAccount(@PathVariable String accountNumber) { // User + Admin
        log.info("Fetching your account details");
        return ResponseEntity.ok(accountService.getMyAccount(accountNumber));
    }

    @Operation(
            summary = "Get all accounts of the user",
            description = "Returns all accounts of the user who is currently logged in"
    )
    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts() { // User + Admin
        log.info("Fetching all of your accounts");
        return ResponseEntity.ok(accountService.getMyAccounts());
    }

    @Operation(
            summary = "Create a new account",
            description = "Create a new account"
    )
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) { // User + Admin
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(accountRequest));
    }

    @Operation(
            summary = "Update account status",
            description = "Update account status to either ACTIVE, CLOSED or FROZEN"
    )
    @PatchMapping("/status")
    public ResponseEntity<AccountResponse> updateStatus(@Valid @RequestBody UpdateAccountRequest updateAccountRequest) { // Admin
        return ResponseEntity.ok(accountService.updateStatus(updateAccountRequest));
    }

}
