package org.example.luckyburger.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.cart.dto.redis.CartMenuRedisResponse;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.exception.CartMenuBadRequestException;
import org.example.luckyburger.domain.cart.repository.CartCacheRepository;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartCacheUserService {
    private final CartCacheRepository cartCacheRepository;

    private final UserEntityFinder userEntityFinder;
    private ShopMenuEntityFinder shopMenuEntityFinder;

    @Transactional
    public void addCartMenu(CartAddMenuRequest request) {
        // 로그인 유저
        User user = userEntityFinder.getLoginUser();

        // shopMenu 및 cartMenus 조회
        ShopMenu shopMenu = shopMenuEntityFinder.getShopMenuById(request.shopMenuId());

        // 점포 검증
        if (cartCacheRepository.isUsedByOtherShop(user.getId(), shopMenu.getShop().getId())) {
            throw new CartMenuBadRequestException();
        }

        // 점포 사용
        cartCacheRepository.useShop(user.getId(), shopMenu.getShop().getId());

        // 카트 생성 및 메뉴 증가
        cartCacheRepository.incrementCartMenuQuantity(user.getId(), request.shopMenuId());

        // 리스트를 토대로 총합 금액 계산
        cartCacheRepository.incrementTotalPrice(user.getId(), shopMenu.getMenu().getPrice());
    }

    @Transactional
    public CartResponse getCartResponse() {
        // 로그인 유저
        User user = userEntityFinder.getLoginUser();

        List<CartMenuRedisResponse> cartMenuRedisResponseList =
                cartCacheRepository.findAllCartMenuRedisResponse(user.getId());

       /*List<CartMenuResponse> cartMenuResponseList = cartMenuRedisResponseList.stream()
                .map(cartMenuRedis->{


                     CartMenuResponse.of
                })

*/
        return null;
    }
}
