package org.example.luckyburger.domain.cart.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.luckyburger.domain.cart.dto.response.CartMenuResponse;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.service.MenuEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CartCacheRepository {
    private static final String CART_KEY_PREFIX = "cart:";
    private static final String USER_KEY_PREFIX = "user:";
    private static final String MENU_KEY_PREFIX = "menu:";
    private static final String SHOP_PREFIX = "shopId";
    private static final String TOTAL_PRICE_PREFIX = "totalPrice";

    private final RedisTemplate<String, Object> redisTemplate;

    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;

    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final MenuEntityFinder menuEntityFinder;
    private final ShopEntityFinder shopEntityFinder;
    private final UserEntityFinder userEntityFinder;

    private HashOperations<String, String, Object> hashOps() {
        return redisTemplate.opsForHash();
    }

    public Optional<CartResponse> findAllCartResponse(Long accountId) {
        String key = buildKey(accountId);

        Map<String, Object> allEntries = hashOps().entries(key);

        // 조회 실패
        if (allEntries.isEmpty())
            return Optional.empty();

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

        Integer totalPriceInt = (Integer) allEntries.get(TOTAL_PRICE_PREFIX);

        // TTL 갱신
        setTTL(accountId, 3L, TimeUnit.MINUTES);

        return Optional.of(CartResponse.of(accountId, cartMenuResponseList, (long) totalPriceInt));
    }

    @Transactional
    public Optional<CartResponse> saveAllCache(Long accountId) {
        String key = buildKey(accountId);

        Map<String, Object> allEntries = hashOps().entries(key);

        User user = userEntityFinder.getUserByAccountId(accountId);

        Integer totalPriceInt = (Integer) allEntries.get(TOTAL_PRICE_PREFIX);

        Cart cart = cartRepository.findById(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.of(user, totalPriceInt)));

        cart.updateTotalPrice(totalPriceInt);

        List<CartMenu> cartMenuList = allEntries.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(MENU_KEY_PREFIX))
                .map(entry -> {
                    String menuKey = entry.getKey().substring(MENU_KEY_PREFIX.length());
                    Long shopMenuId = Long.valueOf(menuKey);
                    Integer quantity = (Integer) entry.getValue();
                    ShopMenu shopMenu = shopMenuEntityFinder.getShopMenuById(shopMenuId);

                    return CartMenu.of(
                            cart,
                            shopMenu,
                            quantity
                    );
                })
                .toList();

        // 데이터 제거
        cartMenuRepository.deleteAllByCartId(cart.getId());
        // DB에 저장
        List<CartMenu> savedCartMenuList = cartMenuRepository.saveAll(cartMenuList);

        return Optional.of(CartResponse.of(cart, savedCartMenuList));
    }

    public void setTTL(Long accountId, long timeout, TimeUnit unit) {
        String key = buildKey(accountId);

        redisTemplate.expire(key, timeout, unit);
    }

    private String buildKey(Long accountId) {
        return CART_KEY_PREFIX + USER_KEY_PREFIX + accountId;
    }

    private String buildHashKey(Long shopMenuId) {
        return MENU_KEY_PREFIX + shopMenuId;
    }

    private ShopMenuCacheResponse getShopMenuCacheResponse(Long shopMenuId) {
        return shopMenuEntityFinder.getShopMenuCacheResponseById(shopMenuId);
    }

    private Menu getMenu(Long menuId) {
        return menuEntityFinder.getMenuById(menuId);
    }

    private Shop getShop(Long shopMenuId) {
        return shopEntityFinder.getShopById(shopMenuId);
    }
}
