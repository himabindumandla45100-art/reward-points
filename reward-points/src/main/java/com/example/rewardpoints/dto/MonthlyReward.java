package com.example.rewardpoints.dto;

public class MonthlyReward {

    private String month;
    private int points;

    public MonthlyReward() {
    }

    public MonthlyReward(String month, int points) {
        this.month = month;
        this.points = points;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
