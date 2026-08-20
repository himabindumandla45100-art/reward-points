package com.example.rewardpoints.service;

import java.time.LocalDate;
import java.util.List;

import com.example.rewardpoints.dto.CustomerRewardResponse;

public interface RewardService {

    List<CustomerRewardResponse> calculateRewards(LocalDate startDate, LocalDate endDate);

    CustomerRewardResponse calculateCustomerRewards(
            Long customerId, LocalDate startDate, LocalDate endDate);
}
