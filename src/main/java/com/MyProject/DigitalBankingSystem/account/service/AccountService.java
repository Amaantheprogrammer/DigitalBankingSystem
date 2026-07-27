package com.MyProject.DigitalBankingSystem.account.service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.MyProject.DigitalBankingSystem.account.dto.AccountRequest;
import com.MyProject.DigitalBankingSystem.account.dto.AccountResponse;
import com.MyProject.DigitalBankingSystem.account.entity.Account;
import com.MyProject.DigitalBankingSystem.account.entity.AccountStatus;
import com.MyProject.DigitalBankingSystem.account.entity.AccountType;
import com.MyProject.DigitalBankingSystem.account.repository.AccountRepository;
import com.MyProject.DigitalBankingSystem.exception.DuplicateResourceException;
import com.MyProject.DigitalBankingSystem.exception.ResourceNotFoundException;
import com.MyProject.DigitalBankingSystem.user.entity.User;
import com.MyProject.DigitalBankingSystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long accountId) {
        Account account = getOrThrow(accountId);
        return modelMapper.map(account, AccountResponse.class);
    }

    @Transactional(readOnly = true)
    public AccountResponse getByAccountNumber(String accountNumber) {
        Account account = getByAccountNumberOrThrow(accountNumber);
        return modelMapper.map(account, AccountResponse.class);
    }
    
    @Transactional(readOnly = true)
    public BigDecimal getBalanceById(Long accountId) {
        Account account = getOrThrow(accountId);
        return account.getBalance();
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalanceByAccountNumber(String accountNumber) {
        Account account = getByAccountNumberOrThrow(accountNumber);
        return account.getBalance();
    }
    
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Account account = modelMapper.map(accountRequest, Account.class);
        account.setAccountNumber(generateAccountNumber(accountRequest.getAccountType()));
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponse.class);
    }
    
    @Transactional
    public AccountResponse updateStatusById(AccountStatus status, Long accountId) {
        Account account = getOrThrow(accountId);
        if (account.getStatus() == status) {
            throw new DuplicateResourceException("Account already has " + status + " status");
        }
        account.setStatus(status);
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponse.class);
    }

    @Transactional
    public AccountResponse updateStatusByAccountNumber(AccountStatus status, String accountNumber) {
        Account account = getByAccountNumberOrThrow(accountNumber);
        if (account.getStatus() == status) {
            throw new DuplicateResourceException("Account already has " + status + " status");
        }
        account.setStatus(status);
        Account savedAccount = accountRepository.save(account);
        return modelMapper.map(savedAccount, AccountResponse.class);
    }

    // ============================================== Private methods ==================================================
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
}
