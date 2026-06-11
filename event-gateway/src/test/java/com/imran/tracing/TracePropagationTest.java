package com.imran.tracing;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TracePropagationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @Test
    void shouldGenerateTraceIdHeader() throws Exception {

        doNothing()
                .when(accountServiceClient)
                .applyTransaction(org.mockito.ArgumentMatchers.any());

        EventRequest request =
                new EventRequest(
                        "evt-trace-001",
                        "acct-trace-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        Instant.now(),
                        "trace-test"
                );

        mockMvc.perform(
                        post("/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Trace-Id"));
    }
}