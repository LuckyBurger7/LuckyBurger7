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
@Transactional
public class EventAdminService {

    private final EventRepository eventRepository;
    private final EventEntityFinder eventEntityFinder;

    /**
     * 이벤트 생성
     *
     * @param request (타이틀, 내용)
     * @return 생성된 이벤트 반환
     */
    public EventResponse createEvent(EventCreateRequest request) {
        Event event = Event.of(request.title(), request.description());
        Event savedEvent = eventRepository.save(event);
        return EventResponse.from(savedEvent);
    }

    /**
     * 이벤트 수정
     *
     * @param eventId
     * @param request (타이틀, 내용)
     * @return 수정된 내용 반환
     */
    public EventResponse updateEvent(Long eventId, EventCreateRequest request) {
        Event event = eventEntityFinder.getEventById(eventId);
        event.updateEvent(request.title(), request.description());
        return EventResponse.from(event);
    }

    /**
     * 이벤트 삭제
     *
     * @param eventId
     */
    public void deleteEvent(Long eventId) {
        Event event = eventEntityFinder.getEventById(eventId);
        event.delete();
    }
}
