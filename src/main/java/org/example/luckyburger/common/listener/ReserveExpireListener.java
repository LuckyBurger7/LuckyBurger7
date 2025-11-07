package org.example.luckyburger.common.listener;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.cart.service.CartCacheUserService;
import org.example.luckyburger.domain.coupon.audit.CouponAuditLogger;
import org.example.luckyburger.domain.coupon.audit.CouponIssueStatus;
import org.example.luckyburger.domain.coupon.redis.CouponKeys;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReserveExpireListener implements MessageListener {
    private static final String COMPENSATE_LUA = """
            local issuedSetKey = KEYS[1]
            local stockKey = KEYS[2]
            local userId = ARGV[1]
            local issued = redis.call('SISMEMBER', issuedSetKey, userId)
            if issued == 0 then
              redis.call('INCR', stockKey)
              return 1
            end
            return 0
            """;

    private final StringRedisTemplate redis;
    private final CouponAuditLogger audit;
    private final CartCacheUserService cartCacheUserService; // DB 저장 서비스

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);

        reserveSaveAllCacheMessage(message);

        if (!expiredKey.startsWith("reserve:coupon:")) return;

        try {
            String[] parts = expiredKey.split(":");
            if (parts.length != 5 || !"user".equals(parts[3])) return;

            long couponId = Long.parseLong(parts[2]);
            String userId = parts[4];

            audit.log(couponId, Long.parseLong(userId), CouponIssueStatus.RESERVE_EXPIRED, "timeout");

            DefaultRedisScript<Long> script = new DefaultRedisScript<>(COMPENSATE_LUA, Long.class);
            Long result = redis.execute(script, List.of(CouponKeys.issuedSetKey(couponId), CouponKeys.stockKey(couponId)), userId);

            if (Long.valueOf(1L).equals(result)) {
                audit.log(couponId, Long.parseLong(userId), CouponIssueStatus.COMPENSATE, "reserve timeout");
            }
        } catch (Exception ignored) {
        }
    }

    private void reserveSaveAllCacheMessage(Message message) {
        String expiredKey = new String(message.getBody());

        String CART_USER_TIMER_PREFIX = "cart:user:timer:";
        if (expiredKey.startsWith(CART_USER_TIMER_PREFIX)) {
            // 데이터 키 추출
            Long accountId = Long.parseLong(expiredKey.replace(CART_USER_TIMER_PREFIX, ""));

            // DB 저장 로직 실행
            cartCacheUserService.saveAllCache(accountId);
        }
    }
}
