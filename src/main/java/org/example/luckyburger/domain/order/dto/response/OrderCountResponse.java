package org.example.luckyburger.domain.order.dto.response;

import lombok.Builder;

@Builder
public record OrderCountResponse(
        long count
) {
    public static OrderCountResponse of(long count) {
        return OrderCountResponse.builder()
                .count(count)
                .build();
    }
}


