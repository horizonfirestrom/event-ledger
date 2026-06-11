package com.imran.client;

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
                .body(transactionRequest)
                .retrieve()
                .toBodilessEntity();
    }
}