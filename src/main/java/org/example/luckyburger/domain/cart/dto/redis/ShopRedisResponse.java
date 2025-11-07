package org.example.luckyburger.domain.cart.dto.redis;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash("shop")
public class ShopRedisResponse implements Serializable {

    @Id
    private Long shopMenuId;

    public static ShopRedisResponse of(Long shopMenuId) {
        return new ShopRedisResponse(shopMenuId);
    }
}
