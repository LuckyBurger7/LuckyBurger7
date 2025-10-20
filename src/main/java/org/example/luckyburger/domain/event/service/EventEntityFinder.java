package org.example.luckyburger.domain.event.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.event.entity.Event;
import org.example.luckyburger.domain.event.exception.NotFoundEventException;
import org.example.luckyburger.domain.event.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EventEntityFinder {

    private final EventRepository eventRepository;

    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                NotFoundEventException::new);
    }
}
