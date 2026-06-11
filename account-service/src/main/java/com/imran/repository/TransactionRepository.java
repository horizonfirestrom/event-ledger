package com.imran.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.imran.entity.Transaction;

import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, String> {

    List<Transaction> findByAccountId(String accountId);
}
