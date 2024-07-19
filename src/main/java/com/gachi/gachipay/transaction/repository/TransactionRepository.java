package com.gachi.gachipay.transaction.repository;

import com.gachi.gachipay.account.entity.Account;
import com.gachi.gachipay.member.entity.Member;
import com.gachi.gachipay.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionId(String transactionId);
}