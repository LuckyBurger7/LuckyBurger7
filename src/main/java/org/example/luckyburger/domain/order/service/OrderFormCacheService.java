package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.dto.cache.OrderFormCache;
import org.example.luckyburger.domain.order.exception.OrderCacheSaveFailedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFormCacheService {

    private final RedisTemplate<String, OrderFormCache> redisTemplate;

    public void saveCache(String redisKey, List<OrderFormCache> orderForms) {
        try {
            redisTemplate.delete(redisKey);
            redisTemplate.opsForList().rightPushAll(redisKey, orderForms);
            redisTemplate.expire(redisKey, Duration.ofMinutes(60));
        } catch (Exception e) {
            // redis 오류 시 Key 삭제 및 예외처리
            redisTemplate.delete(redisKey);
            throw new OrderCacheSaveFailedException();
        }
    }

    public void deleteCache(String redisKey) {
        redisTemplate.delete(redisKey);
    }

    public List<OrderFormCache> getCache(String redisKey) {
        return redisTemplate.opsForList().range(redisKey, 0, -1);
    }
}
