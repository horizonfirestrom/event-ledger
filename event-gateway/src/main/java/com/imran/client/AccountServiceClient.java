package com.imran.client;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.imran.dto.EventRequest;
import com.imran.dto.TransactionRequest;

@Component
public class AccountServiceClient {

    private final RestClient restClient;

    public AccountServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void applyTransaction(EventRequest request) {
    	String traceId = MDC.get("traceId");
    	//System.out.println("Sending TraceId: " + traceId);

        TransactionRequest transactionRequest =
                new TransactionRequest(
                        request.eventId(),
                        request.accountId(),
                        request.type(),
                        request.amount(),
                        request.eventTimestamp());

        restClient.post()
                .uri("/accounts/{accountId}/transactions",
                        request.accountId())
                .header("X-Trace-Id", traceId)
                .body(transactionRequest)
                .retrieve()
                .toBodilessEntity();
    }
}