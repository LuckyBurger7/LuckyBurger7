package org.example.luckyburger.domain.cart.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.luckyburger.domain.cart.exception.CartMenuBadRequestException;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.service.MenuEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CartLuaRepository {
    private static final String CART_KEY_PREFIX = "cart:";
    private static final String USER_KEY_PREFIX = "user:";
    private static final String MENU_KEY_PREFIX = "menu:";
    private static final String SHOP_PREFIX = "shopId";
    private static final String TOTAL_PRICE_PREFIX = "totalPrice";

    private final RedisTemplate<String, Object> redisTemplate;

    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final MenuEntityFinder menuEntityFinder;
    private final ShopEntityFinder shopEntityFinder;

    @Value("classpath:lua/add_cart_menu.lua")
    private Resource addCartMenuResource;

    @Value("classpath:lua/update_cart_menu.lua")
    private Resource updateCartMenuResource;

    @Value("classpath:lua/delete_cart_menu.lua")
    private Resource deleteCartMenuResource;

    public void addCartMenu(Long accountId, Long shopId, Long shopMenuId, Long price, Long timeout) {

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

        Long result = redisTemplate.execute(script, keys, args);

        if (result == 0)
            throw new CartMenuBadRequestException();
    }

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
