package org.example.luckyburger.domain.cart.dto.redis;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("cart")
public class CartRedis implements Serializable {

    @Id
    private String accountId;

    //18000초 = 5시간
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long expirationInSeconds;

    private Long totalPrice;

    private CartRedis(Long accountId) {
        this.accountId = accountId.toString();
        expirationInSeconds = 18000L;
        totalPrice = 0L;
    }

    public static CartRedis of(Long accountId) {
        return new CartRedis(accountId);
    }
}
