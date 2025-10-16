package org.example.luckyburger.domain.user.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.user.entity.User;

@Builder
public record UserResponse(
        Account account,
        String phone,
        String address,
        String street,
        int point
) {
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .account(user.getAccount())
                .phone(user.getPhone())
                .address(user.getAddress())
                .street(user.getStreet())
                .build();
    }
}
