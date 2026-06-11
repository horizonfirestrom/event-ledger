package com.imran.service;

import com.imran.client.AccountServiceClient;
import com.imran.dto.EventRequest;
import com.imran.entity.Event;
import com.imran.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final AccountServiceClient accountServiceClient;

    public EventService(EventRepository eventRepository, AccountServiceClient accountServiceClient) {
        this.eventRepository = eventRepository;
        this.accountServiceClient = accountServiceClient;
    }

    public Event createEvent(EventRequest request) {

        Optional<Event> existingEvent =
                eventRepository.findById(request.eventId());

        if (existingEvent.isPresent()) {
            return existingEvent.get();
        }

        Event event = new Event();

        event.setEventId(request.eventId());
        event.setAccountId(request.accountId());
        event.setType(request.type());
        event.setAmount(request.amount());
        event.setCurrency(request.currency());
        event.setEventTimestamp(request.eventTimestamp());
        event.setMetadata(request.metadata());
        
        Event savedEvent = eventRepository.save(event);

        // Call Account Service
        accountServiceClient.applyTransaction(request);

        return savedEvent;
    }

    public Event getEvent(String eventId) {

        return eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Event not found: " + eventId));
    }

    public List<Event> getEventsByAccount(
            String accountId) {

        return eventRepository
                .findByAccountIdOrderByEventTimestampAsc(
                        accountId);
    }
}