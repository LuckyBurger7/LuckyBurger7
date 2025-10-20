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

    /**
     * 이벤트 존재 여부 확인 (삭제 된 자료도 검출)
     *
     * @param eventId
     * @return 조건에 맞는 이벤트가 없을 경우 NotFoundEventException 반환
     */
    public Event getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                NotFoundEventException::new);
        if (event.getDeletedAt() != null) {
            throw new NotFoundEventException();
        }
        return event;
    }
}
