package org.example.luckyburger.domain.cart.dto.redis;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@RedisHash("cartMenu")
public class CartMenuRedisResponse implements Serializable {

    @Id
    private Long shopMenuId;

    private Integer quantity;

    private CartMenuRedisResponse(Long shopMenuId, Integer quantity) {
        this.shopMenuId = shopMenuId;
        this.quantity = quantity;
    }

    public static CartMenuRedisResponse of(Long shopMenuId, Integer quantity) {
        return new CartMenuRedisResponse(shopMenuId, quantity);
    }
}
