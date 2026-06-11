package com.imran.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.imran.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionRequest(

        @NotBlank
        String eventId,

        @NotBlank
        String accountId,

        @NotNull
        TransactionType type,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        Instant eventTimestamp

) {
}