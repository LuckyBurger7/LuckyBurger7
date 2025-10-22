package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.code.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.ShopNotFoundException;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ShopEntityFinder {

    private final ShopRepository shopRepository;

    public Shop getShopById(Long shopId){

        return shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopNotFoundException());
    }

    public Shop getShopByName(String shopName){

        return shopRepository.findByName(shopName)
                .orElseThrow(() -> new ShopNotFoundException());
    }

}
