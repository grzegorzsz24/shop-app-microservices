package org.example.orderservice.domain.model.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * A simple representation of a monetary amount and currency for database purposes.
 * <p>
 * This class should not be used for calculations or monetary operations.
 * It ensures that the amount has exactly two decimal places and that the currency is valid.
 */

@Embeddable
public record PersistentMoney(

        @Column(nullable = false, precision = 19, scale = 2)
        BigDecimal amount,

        @Column(nullable = false, columnDefinition = "BPCHAR(3)")
        String currency
) {
    public PersistentMoney {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount scale cannot exceed 2");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || currency.isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency code: " + currency, e);
        }
    }
}
