package com.example.rewardpoints.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllCustomers() throws Exception {
        mockMvc.perform(get("/api/rewards")
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void shouldReturnAliceRewards() throws Exception {
        mockMvc.perform(get("/api/rewards/1")
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-03-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId", is(1)))
                .andExpect(jsonPath("$.customerName", is("Alice")))
                .andExpect(jsonPath("$.totalPoints", is(520)));
    }

    @Test
    void shouldRejectInvalidDateRange() throws Exception {
        mockMvc.perform(get("/api/rewards")
                .param("startDate", "2026-03-31")
                .param("endDate", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }
}
