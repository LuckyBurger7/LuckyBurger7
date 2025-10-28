package org.example.luckyburger.domain.shop.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ShopRequest(
        @NotBlank(message = "가게 이름은 필수값입니다.")
        String name,
        @NotBlank(message = "주소는 필수값입니다.")
        String address,
        @NotBlank(message = "상세 주소는 필수값입니다.")
        String street
) {
}
