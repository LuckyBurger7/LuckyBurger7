package org.example.luckyburger.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserAccountResponse(
        String email,

        String name,

        String phone,

        String address,

        String street
) {
    public static UserAccountResponse of(
            String email,
            String name,
            String phone,
            String address,
            String street
    ) {
        return UserAccountResponse.builder()
                .email(email)
                .name(name)
                .phone(phone)
                .address(address)
                .street(street)
                .build();
    }
}
