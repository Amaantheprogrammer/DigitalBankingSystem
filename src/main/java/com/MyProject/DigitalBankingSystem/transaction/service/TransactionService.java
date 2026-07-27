package com.MyProject.DigitalBankingSystem.transaction.service;

import com.MyProject.DigitalBankingSystem.account.entity.AccountStatus;
import com.MyProject.DigitalBankingSystem.exception.DuplicateResourceException;
import com.MyProject.DigitalBankingSystem.exception.InsufficientBalanceException;
import com.MyProject.DigitalBankingSystem.exception.InvalidTransactionException;
import com.MyProject.DigitalBankingSystem.transaction.dto.DepositRequest;
import com.MyProject.DigitalBankingSystem.transaction.dto.TransactionRequest;
import com.MyProject.DigitalBankingSystem.transaction.dto.WithdrawRequest;
import com.MyProject.DigitalBankingSystem.transaction.entity.TransactionStatus;
import com.MyProject.DigitalBankingSystem.transaction.entity.TransactionType;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.MyProject.DigitalBankingSystem.account.entity.Account;
import com.MyProject.DigitalBankingSystem.account.repository.AccountRepository;
import com.MyProject.DigitalBankingSystem.exception.ResourceNotFoundException;
import com.MyProject.DigitalBankingSystem.transaction.dto.TransactionResponse;
import com.MyProject.DigitalBankingSystem.transaction.entity.Transaction;
import com.MyProject.DigitalBankingSystem.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public TransactionResponse getTransactionById(Long transactionId) {
        Transaction transaction = getOrThrow(transactionId);
        return modelMapper.map(transaction, TransactionResponse.class);
    }

    @Transactional
    public TransactionResponse transfer(TransactionRequest transactionRequest) {
        Account senderAccount = getAccountOrThrow(transactionRequest.getSenderAccountNumber());
        Account receiverAccount = getAccountOrThrow(transactionRequest.getReceiverAccountNumber());
        BigDecimal amount = transactionRequest.getAmount();
        if (senderAccount == receiverAccount) {
            throw new DuplicateResourceException("Sender and receiver cannot be same");
        }
        if (senderAccount.getStatus() != AccountStatus.ACTIVE || receiverAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Inactive accounts cannot participate in money transaction");
        }
        if (senderAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in the account: " + senderAccount.getAccountNumber());
        }
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);
        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .amount(amount)
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest depositRequest) {
        Account account = getAccountOrThrow(depositRequest.getAccountNumber());
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Cannot deposit with " + account.getStatus() + " status");
        }
        account.setBalance(account.getBalance().add(depositRequest.getAmount()));
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .amount(depositRequest.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.SUCCESS)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest withdrawRequest) {
        Account account = getAccountOrThrow(withdrawRequest.getAccountNumber());
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Cannot withdraw with " + account.getStatus() + " status");
        }
        if (account.getBalance().subtract(withdrawRequest.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in your account");
        }
        account.setBalance(account.getBalance().subtract(withdrawRequest.getAmount()));
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .amount(withdrawRequest.getAmount())
                .transactionType(TransactionType.WITHDRAW)
                .status(TransactionStatus.SUCCESS)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionResponse.class);
    }

    // ================================================ Private methods ======================================================
    private Transaction getOrThrow(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));
        return transaction;
    }

    private Account getAccountOrThrow(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with account number: " + accountNumber));
        return account;
    }

    private String generateTransactionReference() {
        String reference;
        do {
            String date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String randomPart = UUID.randomUUID().toString().substring(0,6).toUpperCase();
            reference = "TXN-" + date + "-" + randomPart;
        } while (transactionRepository.existsByTransactionReference(reference));
        return reference;
    }
}