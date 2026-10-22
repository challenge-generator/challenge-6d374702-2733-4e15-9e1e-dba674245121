package com.fintech.cleanarchitecture.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction(
        @NotNull(message = "Transaction ID cannot be null")
        UUID id,

        @NotBlank(message = "Account ID cannot be blank")
        String accountId,

        @NotBlank(message = "Transaction type cannot be blank")
        String type,

        @NotNull(message = "Amount cannot be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        BigDecimal amount,

        @NotBlank(message = "Currency cannot be blank")
        String currency,

        @NotNull(message = "Timestamp cannot be null")
        @PastOrPresent(message = "Timestamp cannot be in the future")
        LocalDateTime timestamp,

        @NotBlank(message = "Status cannot be blank")
        String status,

        String description) {

    public Transaction {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    public Transaction withStatus(String newStatus) {
        return new Transaction(this.id, this.accountId, this.type, this.amount, this.currency, this.timestamp, newStatus, this.description);
    }

    public boolean isSuccessful() {
        return "COMPLETED".equalsIgnoreCase(status);
    }

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return "FAILED".equalsIgnoreCase(status);
    }
}