package org.example.luckyburger.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.event.dto.request.EventCreateRequest;
import org.example.luckyburger.domain.event.dto.response.EventResponse;
import org.example.luckyburger.domain.event.entity.Event;
import org.example.luckyburger.domain.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventAdminService {

    private final EventRepository eventRepository;
    private final EventEntityFinder eventEntityFinder;

    @Transactional
    public EventResponse createEvent(EventCreateRequest request) {

        Event event = Event.of(request.title(), request.description());
        Event savedEvent = eventRepository.save(event);
        return EventResponse.from(savedEvent);
    }

    @Transactional
    public EventResponse updateEvent(Long eventId, EventCreateRequest request) {
        Event event = eventEntityFinder.getEvent(eventId);
        event.updateEvent(request.title(), request.description());
        return EventResponse.from(event);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventEntityFinder.getEvent(eventId);
        event.delete();
    }
}
