package org.example.luckyburger.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record AccountResponse(
        Long id,
        String email,
        String name
) {
    public static AccountResponse of(Long id, String email, String name) {
        return AccountResponse.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }
}
