package com.demo.banking_app.domain.model;

import lombok.Value;
import java.math.BigDecimal;
import java.util.Currency;

@Value
public class Money {
    BigDecimal amount;
    Currency currency;
    
    private Money(BigDecimal amount, Currency currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.currency = currency != null ? currency : Currency.getInstance("USD");
    }
    
    public static Money of(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("USD"));
    }
    
    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount, currency);
    }
    
    public static Money zero() {
        return new Money(BigDecimal.ZERO, Currency.getInstance("USD"));
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        BigDecimal result = this.amount.subtract(other.amount);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Result cannot be negative");
        }
        return new Money(result, this.currency);
    }
    
    public boolean isLessThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare money with different currencies");
        }
        return this.amount.compareTo(other.amount) < 0;
    }
    
    public boolean isGreaterThanOrEqual(Money other) {
        return !isLessThan(other);
    }
}

