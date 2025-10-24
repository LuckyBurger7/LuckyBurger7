package org.example.luckyburger.domain.auth.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.auth.entity.Owner;

@Builder
public record OwnerResponse(
        Long id,
        String email,
        String name,
        Long shopId
) {
    public static OwnerResponse from(Owner owner) {
        return OwnerResponse.builder()
                .id(owner.getAccount().getId())
                .email(owner.getAccount().getEmail())
                .name(owner.getAccount().getName())
                .shopId(owner.getShop().getId())
                .build();
    }
}
