package org.example.luckyburger.domain.coupon.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.example.luckyburger.domain.coupon.redis.CouponKeys.*;

@Component
@RequiredArgsConstructor
public class CouponScripts {
    public static final String ISSUE_LUA = """
            local stockKey = KEYS[1]
            local issuedSet = KEYS[2]
            local reserveKey = KEYS[3]
            local gateKey = KEYS[4]
            local userId = ARGV[1]
            local reserveTTL = tonumber(ARGV[2])
            
            if redis.call('EXISTS', gateKey) == 1 then
              return -1
            end
            
            if redis.call('SISMEMBER', issuedSet, userId) == 1 then
              return 2
            end
            
            local stock = tonumber(redis.call('GET', stockKey) or '0')
            if stock <= 0 then return 0 end
            
            if redis.call('SETNX', reserveKey, '1') == 0 then
              return 2
            end
            if reserveTTL and reserveTTL > 0 then
              redis.call('PEXPIRE', reserveKey, reserveTTL)
            end
            
            redis.call('DECR', stockKey)
            return 1
            """;

    public static final String COMPENSATE_LUA = """
            local stockKey    = KEYS[1]
            local reserveKey = KEYS[2]
            
            local deleted = redis.call('DEL', reserveKey)
            if deleted == 1 then
              redis.call('INCR', stockKey)
              return 1
            end
            return 0
            """;

    private final StringRedisTemplate redis;

    /**
     * return: -1 (오픈 전), 0 (매진), 2 (중복/진행 중), 1 (성공)
     **/
    public long reserveAndDecr(long couponId, long userId, long reserveTTL) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(ISSUE_LUA, Long.class);
        return redis.execute(
                script,
                List.of(
                        stockKey(couponId),
                        issuedSetKey(couponId),
                        reserveKey(couponId, userId),
                        gateKey(couponId)
                ),
                String.valueOf(userId),
                String.valueOf(reserveTTL)
        );
    }

    public void confirmIssued(long couponId, long userId) {
        redis.opsForSet().add(issuedSetKey(couponId), String.valueOf(userId));
        redis.delete(reserveKey(couponId, userId));
    }

    public long compensateIfReserved(long couponId, long userId) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(COMPENSATE_LUA, Long.class);
        return redis.execute(
                script,
                List.of(
                        stockKey(couponId),
                        reserveKey(couponId, userId)
                )
        );
    }

    public void setStock(long couponId, long count, Instant expireAt) {
        redis.opsForValue().set(stockKey(couponId), String.valueOf(count));
        if (expireAt != null) {
            redis.execute((RedisConnection conn) -> {
                conn.keyCommands().pExpireAt(stockKey(couponId).getBytes(StandardCharsets.UTF_8), expireAt.toEpochMilli());
                return null;
            });
        }
    }

    public void setGate(long couponId, Instant openAt) {
        if (openAt == null) {
            return;
        }
        if (openAt.isAfter(Instant.now())) {
            redis.opsForValue().set(gateKey(couponId), "1");
            redis.execute((RedisConnection conn) -> {
                conn.keyCommands().pExpireAt(gateKey(couponId).getBytes(StandardCharsets.UTF_8), openAt.toEpochMilli());
                return null;
            });
        } else {
            redis.delete(gateKey(couponId));
        }
    }

    public void setIssuedSet(long couponId, Instant expireAt) {
        redis.opsForSet().add(issuedSetKey(couponId), "INIT_MARKER");
        redis.opsForSet().remove(issuedSetKey(couponId), "INIT_MARKER");
        expireIssuedAt(couponId, expireAt);
    }

    public void expireIssuedAt(long couponId, Instant expireAt) {
        if (expireAt == null) return;
        redis.execute((RedisConnection conn) -> {
            conn.keyCommands().pExpireAt(issuedSetKey(couponId).getBytes(StandardCharsets.UTF_8), expireAt.toEpochMilli());
            return null;
        });
    }

    public void deleteAllKey(long couponId) {
        redis.delete(Arrays.asList(
                CouponKeys.stockKey(couponId),
                CouponKeys.gateKey(couponId),
                CouponKeys.issuedSetKey(couponId)
        ));
        deleteKeysByPattern("reserve:coupon" + couponId + ":user:*");
    }

    public void deleteKeysByPattern(String pattern) {
        redis.execute((RedisConnection conn) -> {
            ScanOptions opt = ScanOptions.scanOptions().match(pattern).count(1000).build();
            try (Cursor<byte[]> cur = conn.scan(opt)) {
                while (cur.hasNext()) {
                    conn.keyCommands().unlink(cur.next()); // 비동기 삭제
                }
            } catch (Exception ignored) {
            }
            return null;
        });
    }
}
