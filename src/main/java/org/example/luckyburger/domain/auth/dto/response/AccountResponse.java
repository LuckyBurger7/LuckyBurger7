package org.example.luckyburger.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.auth.entity.Account;

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

    public static AccountResponse from(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .name(account.getName())
                .build();
    }
}
