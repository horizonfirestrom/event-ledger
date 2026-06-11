package com.imran.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imran.dto.BalanceResponse;
import com.imran.dto.TransactionRequest;
import com.imran.entity.Account;
import com.imran.entity.Transaction;
import com.imran.enums.TransactionType;
import com.imran.repository.AccountRepository;
import com.imran.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void applyTransaction(TransactionRequest request) {

        Account account = accountRepository
                .findById(request.accountId())
                .orElse(new Account(request.accountId()));

        if (request.type() == TransactionType.CREDIT) {

            account.setBalance(
                    account.getBalance().add(request.amount()));

        } else if (request.type() == TransactionType.DEBIT) {

            account.setBalance(
                    account.getBalance().subtract(request.amount()));

        } else {

            throw new IllegalArgumentException(
                    "Unsupported transaction type");
        }

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setEventId(request.eventId());
        transaction.setAccountId(request.accountId());
        transaction.setType(request.type());
        transaction.setAmount(request.amount());
        transaction.setEventTimestamp(request.eventTimestamp());

        transactionRepository.save(transaction);
    }

    public BalanceResponse getBalance(String accountId) {

        Account account = accountRepository
                .findById(accountId)
                .orElse(new Account(accountId));

        return new BalanceResponse(
                account.getAccountId(),
                account.getBalance());
    }

    public Account getAccount(String accountId) {

        return accountRepository
                .findById(accountId)
                .orElse(new Account(accountId));
    }

    public List<Transaction> getTransactions(String accountId) {

        return transactionRepository.findByAccountId(accountId);
    }
}
