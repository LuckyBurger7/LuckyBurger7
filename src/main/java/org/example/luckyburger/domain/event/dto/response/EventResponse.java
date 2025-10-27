package org.example.luckyburger.domain.event.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.event.entity.Event;

import java.time.LocalDateTime;

@Builder
public record EventResponse(
        Long eventId,
        String title,
        String description,
        LocalDateTime createAt,
        LocalDateTime modifiedAt
) {

    public static EventResponse from(Event event) {
        return EventResponse.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .createAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }
}
