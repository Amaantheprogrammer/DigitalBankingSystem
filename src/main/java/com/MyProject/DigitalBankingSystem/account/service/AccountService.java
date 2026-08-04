package com.MyProject.DigitalBankingSystem.account.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.MyProject.DigitalBankingSystem.exception.AccessDeniedException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MyProject.DigitalBankingSystem.account.dto.AccountRequest;
import com.MyProject.DigitalBankingSystem.account.dto.AccountResponse;
import com.MyProject.DigitalBankingSystem.account.dto.UpdateAccountRequest;
import com.MyProject.DigitalBankingSystem.account.entity.Account;
import com.MyProject.DigitalBankingSystem.account.entity.AccountType;
import com.MyProject.DigitalBankingSystem.account.repository.AccountRepository;
import com.MyProject.DigitalBankingSystem.exception.DuplicateResourceException;
import com.MyProject.DigitalBankingSystem.exception.ResourceNotFoundException;
import com.MyProject.DigitalBankingSystem.user.entity.User;
import com.MyProject.DigitalBankingSystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    @Transactional(readOnly = true)
    @Cacheable(value = "accounts", key = "#id")
    public AccountResponse getAccountById(Long accountId) { // Admin
        Account account = getOrThrow(accountId);
        return modelMapper.map(account, AccountResponse.class);
    }

    @Transactional(readOnly = true)
    public AccountResponse getByAccountNumber(String accountNumber) { // Admin
        Account account = getByAccountNumberOrThrow(accountNumber);
        return modelMapper.map(account, AccountResponse.class);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getBalanceById(Long accountId) { // Admin
        Account account = getOrThrow(accountId);
        return account.getBalance();
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalanceByAccountNumber(String accountNumber) { // Admin
        Account account = getByAccountNumberOrThrow(accountNumber);
        return account.getBalance();
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUserId(Long userId) { // Admin
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public AccountResponse getMyAccount(String accountNumber) { // User + Admin
        Account account = getByAccountNumberOrThrow(accountNumber);
        validateUserSecurity(account.getUser(), getSecuredUser());
        return modelMapper.map(account, AccountResponse.class);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getMyAccounts() { // User + Admin
        User securedUser = getSecuredUser();
        List<Account> accounts = accountRepository.findByUserId(securedUser.getId());
        return accounts.stream()
                .map(account -> modelMapper.map(account, AccountResponse.class))
                .toList();
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) { // User + Admin
        User securedUser = getSecuredUser();
        Account account = modelMapper.map(accountRequest, Account.class);
        account.setAccountNumber(generateAccountNumber(accountRequest.getAccountType()));
        account.setBalance(BigDecimal.ZERO);
        account.setUser(securedUser);
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponse.class);
    }
    
    @Transactional
    public AccountResponse updateStatus(UpdateAccountRequest updateAccountRequest) { // Admin
        Account account = getByAccountNumberOrThrow(updateAccountRequest.getAccountNumber());
        validateUserSecurity(account.getUser(), getSecuredUser());
        if (account.getStatus() == updateAccountRequest.getStatus()) {
            throw new DuplicateResourceException("Account already has " + updateAccountRequest.getStatus()+ " status");
        }
        account.setStatus(updateAccountRequest.getStatus());
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponse.class);
    }

    // Private methods
    private Account getOrThrow(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));
        return account;
    }

    private Account getByAccountNumberOrThrow(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));
        return account;
    }

    private String generateAccountNumber(AccountType type) {
        String prefix = switch (type) {
            case SAVINGS -> "SAV";
            case CURRENT -> "CUR";
        };
        String accountNumber;
        do {
            accountNumber = prefix + ThreadLocalRandom.current().nextLong(1_000_000_000L, 9_999_999_999L);
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    private User getSecuredUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private void validateUserSecurity(User user, User securedUser) {
        if (!user.getEmail().equals(securedUser.getEmail())) {
            throw new AccessDeniedException("Cannot access accounts of other users");
        }
    }
}
