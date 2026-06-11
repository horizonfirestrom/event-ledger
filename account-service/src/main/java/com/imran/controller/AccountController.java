package com.imran.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imran.dto.BalanceResponse;
import com.imran.dto.TransactionRequest;
import com.imran.entity.Account;
import com.imran.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<Void> applyTransaction(
            @PathVariable String accountId,
            @RequestBody TransactionRequest request) {

        accountService.applyTransaction(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                accountService.getBalance(accountId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(
            @PathVariable String accountId) {

        return ResponseEntity.ok(
                accountService.getAccount(accountId));
    }
}
