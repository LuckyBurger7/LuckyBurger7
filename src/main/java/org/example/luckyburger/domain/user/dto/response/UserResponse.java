package org.example.luckyburger.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record UserResponse(
        Long id,

        String email,

        String name,

        String phone,

        String address,

        String street
) {
    public static UserResponse of(
            Long id,
            String email,
            String name,
            String phone,
            String address,
            String street
    ) {
        return UserResponse.builder()
                .id(id)
                .email(email)
                .name(name)
                .phone(phone)
                .address(address)
                .street(street)
                .build();
    }
}
