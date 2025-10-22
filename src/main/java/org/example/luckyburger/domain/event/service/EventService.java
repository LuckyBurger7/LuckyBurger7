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
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventEntityFinder eventEntityFinder;

    /**
     * 이벤트 단건 조회
     *
     * @param eventId
     * @return 조건에 맞는 이벤트 단건 반환
     */
    public EventResponse getEventResponse(Long eventId) {
        Event event = eventEntityFinder.getEventById(eventId);
        return EventResponse.from(event);
    }

    /**
     * 이벤트 전체 조회 (삭제된 내용 포함)
     *
     * @param pageable
     * @return 생성 된 이벤트 전체 반환 (페이지네이션 사용)
     */
    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEventResponse(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(EventResponse::from);
    }

    /**
     * 이벤트 전체 조회 (삭제된 내용 미포함)
     *
     * @param pageable
     * @return 생성된 이벤트 전체 반환 (페이지네이션 사용)
     */
    public Page<EventResponse> getAllEventByNotDeletedResponse(Pageable pageable) {
        Page<Event> events = eventRepository.findAllByDeletedAtIsNull(pageable);
        return events.map(EventResponse::from);
    }
}
