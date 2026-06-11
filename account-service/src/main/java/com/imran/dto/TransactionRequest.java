package com.imran.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.imran.enums.TransactionType;

public record TransactionRequest(
        String eventId,
        String accountId,
        TransactionType type,
        BigDecimal amount,
        Instant eventTimestamp
) {
}