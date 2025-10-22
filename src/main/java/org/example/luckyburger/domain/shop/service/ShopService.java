package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.exception.ShopMenuBadRequestException;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ShopService {

    private final ShopEntityFinder shopEntityFinder;
    private final ShopRepository shopRepository;
    private final ShopMenuRepository shopMenuRepository;
    private final ShopMenuEntityFinder shopMenuEntityFinder;

    public Page<ShopResponse> searchShopByName(String shopName, Pageable pageable) {
        Page<Shop> shops = shopRepository.findAllByNameContaining(shopName, pageable);

        return shops.map(ShopResponse::from);
    }

    public Page<ShopMenuResponse> getAllShopMenuByShopIdResponse(Long shopId, Pageable pageable) {

        Page<ShopMenu> shopMenus = shopMenuRepository.findByShopIdAndStatusIsNot(shopId, ShopMenuStatus.DEACTIVATE, pageable);

        return shopMenus.map(ShopMenuResponse::from);
    }

    public ShopMenuResponse getMenuDetail(Long shopId, Long shopMenuId) {
        Shop shop = shopEntityFinder.getShopById(shopId);

        ShopMenu shopMenu = shopMenuEntityFinder.getShopMenuById(shopMenuId);

        if (!shopMenu.getShop().getId().equals(shop.getId())) {
            throw new ShopMenuBadRequestException();
        }

        return ShopMenuResponse.from(shopMenu);
    }
}
