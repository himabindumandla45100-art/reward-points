package com.example.rewardpoints.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    private Long id;
    private Long customerId;
    private BigDecimal amount;
    private LocalDate transactionDate;

    public Transaction() {
    }

    public Transaction(Long id, Long customerId, BigDecimal amount, LocalDate transactionDate) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
