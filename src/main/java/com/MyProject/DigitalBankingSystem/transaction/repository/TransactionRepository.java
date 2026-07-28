package com.MyProject.DigitalBankingSystem.transaction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MyProject.DigitalBankingSystem.account.entity.Account;
import com.MyProject.DigitalBankingSystem.transaction.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionReference(String transactionReference);

    boolean existsByTransactionReference(String transactionReference);

    List<Transaction> findBySenderAccount(Account senderAccount);

    List<Transaction> findBySenderAccountAndTransactionAtAfter(
            Account senderAccount,
            LocalDateTime timestamp
    );

    Page<Transaction> findBySenderAccountOrReceiverAccountOrderByTransactionAtDesc(
            Account senderAccount,
            Account receiverAccount,
            Pageable pageable
    );
}
