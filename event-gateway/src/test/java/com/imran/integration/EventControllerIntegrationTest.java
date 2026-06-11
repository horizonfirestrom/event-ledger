package com.imran.integration;

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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @Test
    void shouldCreateEventAndInvokeAccountService() throws Exception {

        EventRequest request =
                new EventRequest(
                        "evt-int-001",
                        "acct-int-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        Instant.now(),
                        "integration-test"
                );

        doNothing()
                .when(accountServiceClient)
                .applyTransaction(request);

        mockMvc.perform(
                        post("/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("evt-int-001"))
                .andExpect(jsonPath("$.accountId").value("acct-int-001"));

        verify(accountServiceClient, times(1))
                .applyTransaction(request);
    }
}