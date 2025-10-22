package org.example.luckyburger.domain.shop.dto.request;

public record ShopRequest(
        String name,
        String address,
        String street
) {
}
