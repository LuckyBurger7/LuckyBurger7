package org.example.luckyburger.domain.cart.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.luckyburger.domain.cart.dto.redis.CartMenuRedisResponse;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.exception.CartMenuBadRequestException;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;

    private final ObjectMapper objectMapper;


    @Value("classpath:lua/add_cart_menu.lua")
    private Resource addCartMenuResource;

    @Value("classpath:lua/update_cart_menu.lua")
    private Resource updateCartMenuResource;

    @Value("classpath:lua/delete_cart_menu.lua")
    private Resource deleteCartMenuResource;

    @Value("classpath:lua/save_cart_to_cache.lua")
    private Resource saveCartToCacheResource;


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

    public void saveCartToCache(Long accountId, Long timeout) throws JsonProcessingException {
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

        Long result = redisTemplate.execute(script, keys, argsList.toArray());
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
