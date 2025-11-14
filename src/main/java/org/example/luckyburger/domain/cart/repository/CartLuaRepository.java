package org.example.luckyburger.domain.cart.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.luckyburger.common.listener.ReserveExpireListener;
import org.example.luckyburger.domain.cart.dto.redis.CartMenuRedisResponse;
import org.example.luckyburger.domain.cart.dto.response.CartMenuResponse;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.service.MenuEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CartLuaRepository {
    public static final String CART_KEY_PREFIX = "cart:";
    public static final String USER_KEY_PREFIX = "user:";
    public static final String MENU_KEY_PREFIX = "menu:";
    public static final String SHOP_PREFIX = "shopId";
    public static final String TOTAL_PRICE_PREFIX = "totalPrice";
    public static final String TIMER_PREFIX = "timer:";

    private final RedisTemplate<String, Object> redisTemplate;

    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final MenuEntityFinder menuEntityFinder;
    private final ShopEntityFinder shopEntityFinder;
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;
    @Value("classpath:lua/add_cart_menu.lua")
    private Resource addCartMenuResource;
    @Value("classpath:lua/update_cart_menu.lua")
    private Resource updateCartMenuResource;
    @Value("classpath:lua/delete_cart_menu.lua")
    private Resource deleteCartMenuResource;
    @Value("classpath:lua/save_cart_to_cache.lua")
    private Resource saveCartToCacheResource;

    private HashOperations<String, String, Object> hashOps() {
        return redisTemplate.opsForHash();
    }

    public Long addCartMenu(Long accountId, Long shopId, Long shopMenuId, Long price, Long timeout) {

        List<String> keys = Arrays.asList(
                buildKey(accountId),
                SHOP_PREFIX,
                buildHashKey(shopMenuId),
                TOTAL_PRICE_PREFIX);

        Object[] args = new Object[]{
                shopId,
                price,
                TimeUnit.MINUTES.toSeconds(timeout)
        };

        RedisScript<Long> script = RedisScript.of(addCartMenuResource, Long.class);

        return redisTemplate.execute(script, keys, args);
    }

    @Transactional(readOnly = true)
    public boolean updateCartMenuQuantity(Long accountId, Long shopMenuId, Integer quantity, Long timeout) {
        ShopMenuCacheResponse shopMenu = getShopMenuCacheResponse(shopMenuId);
        Menu menu = getMenu(shopMenu.menuId());
        long price = menu.getPrice();

        List<String> keys = Arrays.asList(
                buildKey(accountId),
                buildHashKey(shopMenuId),
                TOTAL_PRICE_PREFIX);

        Object[] args = new Object[]{
                quantity,
                price,
                TimeUnit.MINUTES.toSeconds(timeout)
        };

        RedisScript<Long> script = RedisScript.of(updateCartMenuResource, Long.class);

        Long result = redisTemplate.execute(script, keys, args);

        return result == 1;
    }

    @Transactional(readOnly = true)
    public boolean deleteCartMenuQuantity(Long accountId, Long shopMenuId, Long timeout) {
        ShopMenuCacheResponse shopMenu = getShopMenuCacheResponse(shopMenuId);
        Menu menu = getMenu(shopMenu.menuId());
        long price = menu.getPrice();

        List<String> keys = Arrays.asList(
                buildKey(accountId),
                buildHashKey(shopMenuId),
                TOTAL_PRICE_PREFIX);

        Object[] args = new Object[]{
                price,
                TimeUnit.MINUTES.toSeconds(timeout)
        };

        RedisScript<Long> script = RedisScript.of(deleteCartMenuResource, Long.class);

        Long result = redisTemplate.execute(script, keys, args);

        return result == 1;
    }

    @Transactional(readOnly = true)
    public void saveCartToCache(Long accountId, Long timeout) {
        Cart cart = cartEntityFinder.getCartByUserId(accountId);
        List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(accountId);

        if (cartMenus.isEmpty())
            return;

        List<CartMenuRedisResponse> cartMenusRedisResponse = cartMenus.stream()
                .map(cartMenu ->
                        CartMenuRedisResponse.of(cartMenu.getShopMenu().getId(), (long) cartMenu.getQuantity()))
                .toList();

        long totalPrice = cart.getTotalPrice();
        long shopId = cartMenus.get(0).getShopMenu().getShop().getId();

        List<Object> argsList = new ArrayList<>(
                Arrays.asList(
                        shopId,
                        totalPrice,
                        TimeUnit.MINUTES.toSeconds(timeout)
                )
        );

        for (CartMenuRedisResponse menu : cartMenusRedisResponse) {
            argsList.add(menu.getShopMenuId());
            argsList.add(menu.getQuantity());
        }

        List<String> keys = Arrays.asList(
                buildKey(accountId),
                MENU_KEY_PREFIX,
                SHOP_PREFIX,
                TOTAL_PRICE_PREFIX);

        RedisScript<Long> script = RedisScript.of(saveCartToCacheResource, Long.class);

        redisTemplate.execute(script, keys, argsList.toArray());
    }

    /**
     * <p>Write Back 저장 시점을 위한 TTL 설정</p>
     * <p>함수 호출 시 정해둔 timeoutMinute분 의 90퍼센트가 지날 때 콜백을 통한 저장 호출</p>
     * {@link ReserveExpireListener}
     *
     * @param accountId     계정 아이디
     * @param timeoutMinute 분 단위 타임 아웃
     */
    public void setSaveDBTimer(Long accountId, Long timeoutMinute) {
        long millis = TimeUnit.MINUTES.toMillis(timeoutMinute);
        // TTL 만료 시간 90%로 설정
        long saveTime = (long) (millis - (millis * 0.1));

        String key = CART_KEY_PREFIX + USER_KEY_PREFIX + TIMER_PREFIX + accountId;
        redisTemplate.opsForValue().set(key, accountId.toString());
        redisTemplate.expire(key, saveTime, TimeUnit.MILLISECONDS);
    }

    public void deleteSaveDBTimer(Long accountId) {
        String key = CART_KEY_PREFIX + USER_KEY_PREFIX + TIMER_PREFIX + accountId;
        redisTemplate.delete(key);
    }


    public Optional<CartResponse> findAllCartResponse(Long accountId) {
        String key = buildKey(accountId);

        Map<String, Object> allEntries = hashOps().entries(key);

        // 조회 실패
        if (allEntries.isEmpty())
            return Optional.empty();

        // DTO 변환
        List<CartMenuResponse> cartMenuResponseList = allEntries.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(MENU_KEY_PREFIX))
                .map(entry -> {
                    String menuKey = entry.getKey().substring(MENU_KEY_PREFIX.length());
                    Long shopMenuId = Long.valueOf(menuKey);
                    Integer quantity = (Integer) entry.getValue();

                    ShopMenuCacheResponse shopMenu = getShopMenuCacheResponse(shopMenuId);
                    Menu menu = getMenu(shopMenu.menuId());
                    Shop shop = getShop(shopMenu.shopId());

                    return CartMenuResponse.of(
                            shopMenuId,
                            menu.getName(),
                            shop.getName(),
                            quantity,
                            menu.getPrice());
                })
                .toList();

        // TTL 갱신
        setTTL(accountId, 3L, TimeUnit.MINUTES);

        // 총 금액 계산
        Integer totalPriceInt = (Integer) allEntries.get(TOTAL_PRICE_PREFIX);

        return Optional.of(CartResponse.of(accountId, cartMenuResponseList, (long) totalPriceInt));
    }

    public Optional<Map<String, Object>> getAllEntriesById(Long accountId) {
        String key = buildKey(accountId);
        Map<String, Object> allEntries = hashOps().entries(key);
        if (allEntries.isEmpty())
            return Optional.empty();
        return Optional.of(allEntries);
    }

    public void setTTL(Long accountId, long timeout, TimeUnit unit) {
        String key = buildKey(accountId);

        redisTemplate.expire(key, timeout, unit);
    }

    private ShopMenuCacheResponse getShopMenuCacheResponse(Long shopMenuId) {
        return shopMenuEntityFinder.getShopMenuCacheResponseById(shopMenuId);
    }

    private String buildKey(Long accountId) {
        return CART_KEY_PREFIX + USER_KEY_PREFIX + accountId;
    }

    private String buildHashKey(Long shopMenuId) {
        return MENU_KEY_PREFIX + shopMenuId;
    }

    private Menu getMenu(Long menuId) {
        return menuEntityFinder.getMenuById(menuId);
    }

    private Shop getShop(Long shopMenuId) {
        return shopEntityFinder.getShopById(shopMenuId);
    }
}
