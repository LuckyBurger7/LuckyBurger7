package org.example.luckyburger.domain.cart.dto.redis;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartMenuRedisResponse {

    private Long shopMenuId;

    private Long quantity;

    public static CartMenuRedisResponse of(Long shopMenuId, Long quantity) {
        return new CartMenuRedisResponse(shopMenuId, quantity);
    }
}
