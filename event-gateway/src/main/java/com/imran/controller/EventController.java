package com.imran.controller;

import com.imran.dto.EventRequest;
import com.imran.entity.Event;
import com.imran.service.EventService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(
            EventService eventService) {

        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(
            @Valid @RequestBody EventRequest request) {

        Event event = eventService.createEvent(request);

        return ResponseEntity.ok(event);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(
            @PathVariable String eventId) {

        return ResponseEntity.ok(
                eventService.getEvent(eventId));
    }

    @GetMapping
    public ResponseEntity<List<Event>> getEvents(
            @RequestParam String account) {

        return ResponseEntity.ok(
                eventService.getEventsByAccount(account));
    }
}