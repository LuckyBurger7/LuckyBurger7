package org.example.luckyburger.domain.event.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.example.luckyburger.domain.event.entity.Event;

@Builder
public record EventResponse(
        Long id,
        String title,
        String description,
        LocalDateTime createAt,
        LocalDateTime modifiedAt
) {

    public static EventResponse from(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .createAt(event.getCreatedAt())
                .modifiedAt(event.getModifiedAt())
                .build();
    }
}
