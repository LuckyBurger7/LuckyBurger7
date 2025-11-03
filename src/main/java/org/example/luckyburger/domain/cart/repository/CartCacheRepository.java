package org.example.luckyburger.domain.cart.repository;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.cart.dto.redis.CartMenuRedisResponse;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CartCacheRepository {
    private static final String CART_KEY_PREFIX = "cart:";
    private static final String USER_KEY_PREFIX = "user:";
    private static final String MENU_KEY_PREFIX = "menu:";
    private static final String SHOP_PREFIX = "shopId";
    private static final String TOTAL_PRICE_PREFIX = "totalPrice";

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Object> hashOps() {
        return redisTemplate.opsForHash();
    }

    public void incrementCartMenuQuantity(Long accountId, Long shopMenuId) {
        String key = buildKey(accountId);
        String hashKey = buildHashKey(shopMenuId);

        hashOps().increment(key, hashKey, 1);
    }

    public void decrementCartMenuQuantity(Long accountId, Long shopMenuId) {
        String key = buildKey(accountId);
        String hashKey = buildHashKey(shopMenuId);

        Long quantity = hashOps().increment(key, hashKey, -1);
        if (quantity <= 0) {
            hashOps().delete(key, hashKey);
            // todo: 전체 조회해서 제거
        }
    }

    public Optional<Integer> findCartMenuQuantityById(Long accountId, Long shopMenuId) {
        String key = buildKey(accountId);
        String hashKey = buildHashKey(shopMenuId);

        Object value = hashOps().get(key, hashKey);

        if (value instanceof Integer)
            return Optional.of((Integer) value);

        return Optional.empty();
    }

    public void useShop(Long accountId, Long shopId) {
        String key = buildKey(accountId);
        hashOps().put(key, SHOP_PREFIX, shopId);
    }

    // 다른 매장에서 사용중.
    public boolean isUsedByOtherShop(Long accountId, Long shopId) {
        String key = buildKey(accountId);

        Object value = hashOps().get(key, SHOP_PREFIX);

        if (value instanceof Long id)
            return id.longValue() != shopId.longValue();

        return false;
    }

    public void incrementTotalPrice(Long accountId, Long price) {
        String key = buildKey(accountId);

        hashOps().increment(key, TOTAL_PRICE_PREFIX, price);
    }

    public void decrementTotalPrice(Long accountId, Long price) {
        String key = buildKey(accountId);

        hashOps().increment(key, TOTAL_PRICE_PREFIX, -price);
    }

    public List<CartMenuRedisResponse> findAllCartMenuRedisResponse(Long accountId) {
        String key = buildKey(accountId);

        Map<String, Object> allEntries = hashOps().entries(key);

        return allEntries.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("menu:"))
                .map(entry -> {
                    String menuKey = entry.getKey().substring("menu:".length());
                    Long menuId = Long.valueOf(menuKey);
                    Integer quantity = (Integer) entry.getValue();

                    return CartMenuRedisResponse.of(menuId, quantity);
                })
                .collect(Collectors.toList());
    }

    private String buildKey(Long accountId) {
        return CART_KEY_PREFIX + USER_KEY_PREFIX + accountId;
    }

    private String buildHashKey(Long shopMenuId) {
        return MENU_KEY_PREFIX + shopMenuId;
    }
}
