package com.imran.service;

import com.imran.dto.EventRequest;
import com.imran.entity.Event;
import com.imran.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(EventRequest request) {

        Event event = new Event();

        event.setEventId(request.eventId());
        event.setAccountId(request.accountId());
        event.setType(request.type());
        event.setAmount(request.amount());
        event.setCurrency(request.currency());
        event.setEventTimestamp(request.eventTimestamp());
        event.setMetadata(request.metadata());

        return eventRepository.save(event);
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