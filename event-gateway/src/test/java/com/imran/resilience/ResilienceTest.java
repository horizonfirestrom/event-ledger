package com.imran.resilience;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imran.client.AccountServiceClient;
import com.imran.dto.EventRequest;
import com.imran.enums.TransactionType;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ResilienceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @Test
    void shouldReturn503WhenAccountServiceUnavailable() throws Exception {

        doThrow(new RuntimeException("Account Service Down"))
                .when(accountServiceClient)
                .applyTransaction(any());

        EventRequest request =
                new EventRequest(
                        "evt-fail-001",
                        "acct-fail-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        Instant.now(),
                        "failure-test"
                );

        mockMvc.perform(
                        post("/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isServiceUnavailable());
    }
}