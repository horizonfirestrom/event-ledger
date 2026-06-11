package com.imran.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.imran.entity.Account;

public interface AccountRepository
        extends JpaRepository<Account, String> {
}