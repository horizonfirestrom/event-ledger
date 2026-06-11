package com.imran.service;

import com.imran.client.AccountServiceClient;
import com.imran.dto.EventRequest;
import com.imran.entity.Event;
import com.imran.repository.EventRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
	private static final Logger log =
            LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final AccountServiceClient accountServiceClient;

    public EventService(EventRepository eventRepository, AccountServiceClient accountServiceClient) {
        this.eventRepository = eventRepository;
        this.accountServiceClient = accountServiceClient;
    }

    public Event createEvent(EventRequest request) {
    	log.info("Processing event {} for account {}",
                request.eventId(),
                request.accountId());
    	

        Optional<Event> existingEvent =
                eventRepository.findById(request.eventId());

        if (existingEvent.isPresent()) {
        	log.info("Duplicate event detected for eventId={}, ignoring", request.eventId());
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
        
        log.info("Event saved successfully with eventId={}",
                request.eventId());

        // Call Account Service
        accountServiceClient.applyTransaction(request);
        log.info("Transaction request sent to Account Service for eventId={}",
                request.eventId());

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