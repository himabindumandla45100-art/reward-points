package com.example.rewardpoints.dto;

import java.util.List;

public class CustomerRewardResponse {

    private Long customerId;
    private String customerName;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;

    public CustomerRewardResponse() {
    }

    public CustomerRewardResponse(Long customerId, String customerName,
                                  List<MonthlyReward> monthlyRewards, int totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<MonthlyReward> getMonthlyRewards() {
        return monthlyRewards;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
