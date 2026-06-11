package com.imran;

import com.imran.client.AccountServiceClient;
import com.imran.dto.EventRequest;
import com.imran.entity.Event;
import com.imran.enums.TransactionType;
import com.imran.repository.EventRepository;
import com.imran.service.EventService;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private EventRepository eventRepository;
    private AccountServiceClient accountServiceClient;
    private EventService eventService;

    @BeforeEach
    void setup() {

        eventRepository = mock(EventRepository.class);
        accountServiceClient = mock(AccountServiceClient.class);

        eventService = new EventService(
                eventRepository,
                accountServiceClient,
                new SimpleMeterRegistry());
    }

    @Test
    void shouldCreateNewEvent() {

        EventRequest request =
                new EventRequest(
                        "evt-001",
                        "acct-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        Instant.now(),
                        "test-metadata"
                );

        Event savedEvent = new Event();

        savedEvent.setEventId("evt-001");
        savedEvent.setAccountId("acct-001");

        when(eventRepository.findById("evt-001"))
                .thenReturn(Optional.empty());

        when(eventRepository.save(any(Event.class)))
                .thenReturn(savedEvent);

        Event result = eventService.createEvent(request);

        assertNotNull(result);
        assertEquals("evt-001", result.getEventId());

        verify(eventRepository, times(1))
                .save(any(Event.class));

        verify(accountServiceClient, times(1))
                .applyTransaction(request);
    }

    @Test
    void shouldReturnExistingEventForDuplicateEventId() {

        Event existingEvent = new Event();

        existingEvent.setEventId("evt-001");
        existingEvent.setAccountId("acct-001");

        EventRequest request =
                new EventRequest(
                        "evt-001",
                        "acct-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        Instant.now(),
                        "test-metadata"
                );

        when(eventRepository.findById("evt-001"))
                .thenReturn(Optional.of(existingEvent));

        Event result = eventService.createEvent(request);

        assertNotNull(result);
        assertEquals("evt-001", result.getEventId());

        verify(eventRepository, never())
                .save(any(Event.class));

        verify(accountServiceClient, never())
                .applyTransaction(any());
    }

    @Test
    void shouldReturnEventById() {

        Event event = new Event();

        event.setEventId("evt-100");

        when(eventRepository.findById("evt-100"))
                .thenReturn(Optional.of(event));

        Event result = eventService.getEvent("evt-100");

        assertNotNull(result);
        assertEquals("evt-100", result.getEventId());
    }

    @Test
    void shouldThrowExceptionWhenEventNotFound() {

        when(eventRepository.findById("evt-404"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> eventService.getEvent("evt-404"));

        assertTrue(
                exception.getMessage()
                        .contains("Event not found"));
    }

    @Test
    void shouldIncrementMetricWhenEventProcessed() {

        EventRequest request =
                new EventRequest(
                        "evt-metric",
                        "acct-001",
                        TransactionType.CREDIT,
                        BigDecimal.valueOf(100),
                        "USD",
                        Instant.now(),
                        "metric-test"
                );

        Event savedEvent = new Event();
        savedEvent.setEventId("evt-metric");

        when(eventRepository.findById("evt-metric"))
                .thenReturn(Optional.empty());

        when(eventRepository.save(any(Event.class)))
                .thenReturn(savedEvent);

        assertDoesNotThrow(
                () -> eventService.createEvent(request));

        verify(eventRepository, times(1))
                .save(any(Event.class));

        verify(accountServiceClient, times(1))
                .applyTransaction(request);
    }
}