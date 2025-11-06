package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.consts.CacheConst;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.exception.ShopMenuNotFoundException;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.example.luckyburger.domain.statistic.dto.response.MenuTotalSalesResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ShopMenuEntityFinder {

    private final ShopMenuRepository shopMenuRepository;

    // Fetch 적용
    public ShopMenu getShopMenuByIdDetails(Long shopMenuId) {
        return shopMenuRepository.findByIdWithDetails(shopMenuId)
                .orElseThrow(ShopMenuNotFoundException::new);
    }

    // Fetch 미적용
    public ShopMenu getShopMenuById(Long shopMenuId) {
        return shopMenuRepository.findByIdWithDetails(shopMenuId)
                .orElseThrow(ShopMenuNotFoundException::new);
    }

    @Cacheable(value = CacheConst.SHOP_MENU, key = "#shopMenuId")
    public ShopMenuCacheResponse getShopMenuCacheResponseById(Long shopMenuId) {
        return shopMenuRepository.findShopMenuCacheResponseById(shopMenuId)
                .orElseThrow(ShopMenuNotFoundException::new);
    }

    public List<MenuTotalSalesResponse> getAllMenuTotalSalesByMenuIds(List<Long> menuIds) {
        return shopMenuRepository.findAllMenuTotalSalesResponseByMenuIds(menuIds);
    }

    public Long getSumOfSalesVolumes() {
        return shopMenuRepository.findSumOfSalesVolumes();
    }
}
