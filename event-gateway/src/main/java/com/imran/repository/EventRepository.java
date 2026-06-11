package com.imran.repository;

import com.imran.entity.Event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository
        extends JpaRepository<Event, String> {

    List<Event> findByAccountIdOrderByEventTimestampAsc(
            String accountId);

    boolean existsByEventId(String eventId);
    Optional<Event> findByEventId(String eventId);
}