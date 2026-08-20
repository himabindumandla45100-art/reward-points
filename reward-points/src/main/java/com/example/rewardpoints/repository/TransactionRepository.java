package com.example.rewardpoints.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rewardpoints.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdAndTransactionDateBetween(
            Long customerId, LocalDate startDate, LocalDate endDate);
}
