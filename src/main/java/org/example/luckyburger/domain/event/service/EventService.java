package org.example.luckyburger.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.event.dto.response.EventResponse;
import org.example.luckyburger.domain.event.entity.Event;
import org.example.luckyburger.domain.event.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final EventEntityFinder eventEntityFinder;

    @Transactional(readOnly = true)
    public EventResponse getEvent(Long eventId) {
        Event event = eventEntityFinder.getEvent(eventId);
        return EventResponse.from(event);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvent(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(EventResponse::from);
    }
}
