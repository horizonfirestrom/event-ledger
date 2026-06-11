package com.imran.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<?> handleDuplicateTransaction(
            DuplicateTransactionException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "timestamp", Instant.now(),
                        "status", 409,
                        "error", "Duplicate Transaction",
                        "message", ex.getMessage()
                ));
    }
}