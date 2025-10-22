package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.exception.ShopMenuNotFoundException;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ShopMenuEntityFinder {

    private final ShopMenuRepository shopMenuRepository;

    public ShopMenu getShopMenuById(Long shopMenuId) {
        return shopMenuRepository.findById(shopMenuId)
                .orElseThrow(ShopMenuNotFoundException::new);
    }
}
