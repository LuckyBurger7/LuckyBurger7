package org.example.luckyburger.domain.shop.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;

@Builder
public record ShopMenuCacheResponse(Long shopId, Long menuId, Long price, ShopMenuStatus shopMenuStatus) {

    public static ShopMenuCacheResponse of(Long shopId, Long menuId, Long price, ShopMenuStatus shopMenuStatus) {
        return ShopMenuCacheResponse.builder()
                .shopId(shopId)
                .menuId(menuId)
                .price(price)
                .shopMenuStatus(shopMenuStatus)
                .build();
    }
}
