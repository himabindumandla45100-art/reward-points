package com.example.rewardpoints.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.rewardpoints.dto.CustomerRewardResponse;
import com.example.rewardpoints.dto.MonthlyReward;
import com.example.rewardpoints.entity.Customer;
import com.example.rewardpoints.entity.Transaction;
import com.example.rewardpoints.exception.InvalidDateRangeException;
import com.example.rewardpoints.repository.CustomerRepository;
import com.example.rewardpoints.repository.TransactionRepository;

@Service
public class RewardServiceImpl implements RewardService {

    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public RewardServiceImpl(CustomerRepository customerRepository,
                             TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<CustomerRewardResponse> calculateRewards(
            LocalDate startDate, LocalDate endDate) {

        validateDateRange(startDate, endDate);

        return customerRepository.findAll().stream()
                .map(customer -> buildResponse(customer, startDate, endDate))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerRewardResponse calculateCustomerRewards(
            Long customerId, LocalDate startDate, LocalDate endDate) {

        validateDateRange(startDate, endDate);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Customer not found: " + customerId));

        return buildResponse(customer, startDate, endDate);
    }

    public int calculatePoints(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Transaction amount cannot be null or negative");
        }

        if (amount.compareTo(FIFTY) <= 0) {
            return 0;
        }

        if (amount.compareTo(HUNDRED) <= 0) {
            return amount.subtract(FIFTY).intValue();
        }

        return 50 + amount.subtract(HUNDRED)
                .multiply(BigDecimal.valueOf(2))
                .intValue();
    }

    private CustomerRewardResponse buildResponse(
            Customer customer, LocalDate startDate, LocalDate endDate) {

        List<Transaction> transactions =
                transactionRepository.findByCustomerIdAndTransactionDateBetween(
                        customer.getId(), startDate, endDate);

        Map<String, Integer> monthlyPoints = new LinkedHashMap<>();

        for (Transaction transaction : transactions) {
            String month = transaction.getTransactionDate()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM"));

            monthlyPoints.merge(
                    month,
                    calculatePoints(transaction.getAmount()),
                    Integer::sum);
        }

        List<MonthlyReward> monthlyRewards = new ArrayList<>();

        monthlyPoints.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        monthlyRewards.add(
                                new MonthlyReward(entry.getKey(), entry.getValue())));

        int total = monthlyRewards.stream()
                .mapToInt(MonthlyReward::getPoints)
                .sum();

        return new CustomerRewardResponse(
                customer.getId(),
                customer.getName(),
                monthlyRewards,
                total);
    }

  
    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException(
                    "Start date and end date are required");
        }

        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException(
                    "Start date cannot be after end date");
        }
    }
}
