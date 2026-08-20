package com.example.rewardpoints.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.rewardpoints.dto.CustomerRewardResponse;
import com.example.rewardpoints.entity.Customer;
import com.example.rewardpoints.entity.Transaction;
import com.example.rewardpoints.exception.InvalidDateRangeException;
import com.example.rewardpoints.repository.CustomerRepository;
import com.example.rewardpoints.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private RewardServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RewardServiceImpl(
                customerRepository, transactionRepository);
    }

    @Test
    void shouldReturnZeroForAmountAtOrBelowFifty() {
        assertEquals(0, service.calculatePoints(BigDecimal.valueOf(50)));
        assertEquals(0, service.calculatePoints(BigDecimal.valueOf(40)));
    }

    @Test
    void shouldCalculatePointsBetweenFiftyAndHundred() {
        assertEquals(30, service.calculatePoints(BigDecimal.valueOf(80)));
        assertEquals(50, service.calculatePoints(BigDecimal.valueOf(100)));
    }

    @Test
    void shouldCalculatePointsAboveHundred() {
        assertEquals(90, service.calculatePoints(BigDecimal.valueOf(120)));
        assertEquals(250, service.calculatePoints(BigDecimal.valueOf(200)));
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculatePoints(BigDecimal.valueOf(-1)));
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> service.calculatePoints(null));
    }

    @Test
    void shouldRejectInvalidDateRange() {
        assertThrows(InvalidDateRangeException.class,
                () -> service.calculateRewards(
                        LocalDate.of(2026, 3, 1),
                        LocalDate.of(2026, 1, 1)));
    }

    @Test
    void shouldCalculateMultipleMonthlyTransactions() {
        Customer customer = new Customer(1L, "Alice");

        Transaction t1 = new Transaction(
                1L, 1L, BigDecimal.valueOf(120),
                LocalDate.of(2026, 1, 10));

        Transaction t2 = new Transaction(
                2L, 1L, BigDecimal.valueOf(80),
                LocalDate.of(2026, 1, 15));

        Transaction t3 = new Transaction(
                3L, 1L, BigDecimal.valueOf(150),
                LocalDate.of(2026, 2, 10));

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 31)))
                .thenReturn(List.of(t1, t2, t3));

        CustomerRewardResponse response =
                service.calculateCustomerRewards(
                        1L,
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 31));

        assertEquals(2, response.getMonthlyRewards().size());
        assertEquals(120, response.getMonthlyRewards().get(0).getPoints());
        assertEquals(150, response.getMonthlyRewards().get(1).getPoints());
        assertEquals(270, response.getTotalPoints());
    }

    @Test
    void shouldReturnZeroForCustomerWithoutTransactions() {
        Customer customer = new Customer(1L, "Alice");

        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                1L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 31)))
                .thenReturn(Collections.emptyList());

        CustomerRewardResponse response =
                service.calculateCustomerRewards(
                        1L,
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 3, 31));

        assertEquals(0, response.getTotalPoints());
    }
}
