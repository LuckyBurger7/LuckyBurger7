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
     * 이벤트가 있는지 확인
     *
     * @param eventId
     * @return 조건에 맞는 이벤트가 없을 경우 NotFoundEventException 반환
     */
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                NotFoundEventException::new);
    }
}
