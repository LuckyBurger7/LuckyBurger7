package org.example.luckyburger.domain.shop.dto.response;


import lombok.Builder;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;

@Builder
public record ShopResponse (String name,
                            BusinessStatus businessStatus,
                            String address,
                            String street){

    public static ShopResponse from(Shop shop){

        return ShopResponse.builder()
                .name(shop.getName())
                .businessStatus(shop.getStatus())
                .address(shop.getAddress())
                .street(shop.getStreet())
                .build();
    }

}
