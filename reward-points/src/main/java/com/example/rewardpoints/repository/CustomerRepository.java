package com.example.rewardpoints.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rewardpoints.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
