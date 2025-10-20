package org.example.luckyburger.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.user.entity.User;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record UserResponse(
        Long id,

        String email,

        String name,

        String phone,

        String address,

        String street,

        LocalDateTime createAt
) {
    public static UserResponse of(
            Long id,
            String email,
            String name,
            String phone,
            String address,
            String street,
            LocalDateTime createAt
    ) {
        return UserResponse.builder()
                .id(id)
                .email(email)
                .name(name)
                .phone(phone)
                .address(address)
                .street(street)
                .createAt(createAt)
                .build();
    }

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getAccount().getEmail())
                .name(user.getAccount().getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .street(user.getStreet())
                .createAt(user.getAccount().getCreatedAt())
                .build();
    }
}
