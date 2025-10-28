package org.example.luckyburger.domain.order.dto.request;

import jakarta.validation.constraints.*;

public record OrderCreateRequest(
        @NotNull(message = "가게 ID는 필수값입니다.")
        Long shopId,
        @NotBlank(message = "수령인 이름은 필수값입니다.")
        @Size(max = 50, message = "수령인 이름은 50자 이내여야 합니다.")
        String receiver,
        @NotBlank(message = "전화번호는 필수값입니다.")
        @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        String phone,
        @NotBlank(message = "주소는 필수값입니다.")
        @Size(max = 255, message = "주소는 255자 이내여야 합니다.")
        String address,
        @NotBlank(message = "상세 주소는 필수값입니다.")
        @Size(max = 100, message = "상세 주소는 100자 이내여야 합니다.")
        String street,
        @Size(max = 255, message = "요청사항은 255자 이내여야 합니다.")
        String request,
        Long couponId,
        @PositiveOrZero(message = "포인트는 0 이상이어야 합니다.")
        Integer point
) {
}


