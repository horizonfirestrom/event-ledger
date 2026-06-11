package com.imran;

import com.imran.dto.TransactionRequest;
import com.imran.entity.Account;
import com.imran.enums.TransactionType;
import com.imran.repository.AccountRepository;
import com.imran.repository.TransactionRepository;
import com.imran.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @BeforeEach
    void setup() {

        accountRepository = mock(AccountRepository.class);
        transactionRepository = mock(TransactionRepository.class);

        accountService = new AccountService(
                accountRepository,
                transactionRepository);
    }

    @Test
    void shouldApplyCreditTransaction() {

        TransactionRequest request =
                new TransactionRequest(
                        "evt-001",
                        "acct-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        Instant.now());

        when(accountRepository.findById("acct-001"))
                .thenReturn(Optional.of(new Account("acct-001")));

        accountService.applyTransaction(request);

        verify(accountRepository, times(1))
                .save(any(Account.class));

        verify(transactionRepository, times(1))
                .save(any());
    }
}