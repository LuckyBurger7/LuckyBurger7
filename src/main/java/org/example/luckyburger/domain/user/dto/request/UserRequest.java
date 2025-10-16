package org.example.luckyburger.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.auth.entity.Account;

@Builder(access = AccessLevel.PRIVATE)
public record UserRequest(
        @NotBlank
        Account account,

        @NotBlank
        String phone,

        @NotBlank
        String address,

        @NotBlank
        String street
) {
    public static UserRequest of(Account account, String phone, String address, String street) {
        return UserRequest.builder()
                .account(account)
                .phone(phone)
                .address(address)
                .street(street)
                .build();
    }
}
