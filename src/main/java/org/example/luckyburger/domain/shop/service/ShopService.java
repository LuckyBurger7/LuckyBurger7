package org.example.luckyburger.domain.shop.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public Shop getShopById(Long shopId) {
        return shopRepository.findById(shopId).orElseThrow(
                () -> new IllegalArgumentException("점포가 존재하지 않습니다. id=" + shopId));
    }
}
