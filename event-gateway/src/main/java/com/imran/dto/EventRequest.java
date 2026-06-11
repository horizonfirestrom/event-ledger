package com.imran.dto;

import com.imran.enums.TransactionType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record EventRequest(

        @NotBlank
        String eventId,

        @NotBlank
        String accountId,

        @NotNull
        TransactionType type,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotBlank
        String currency,

        @NotNull
        Instant eventTimestamp,

        String metadata

) {
}
