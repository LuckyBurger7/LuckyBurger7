package org.example.luckyburger.domain.shop.service;

import lombok.AllArgsConstructor;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.exception.shop.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.shop.ShopException;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ShopEntityFinder {

    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public Shop getShopById(Long shopId){
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

        return shop;
    }


}
