package org.example.luckyburger.domain.order.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderPrepareResponse(
        String shopName,
        String receiver,
        String phone,
        String address,
        String street,
        List<OrderCouponResponse> coupons,
        Integer point,
        long totalPrice,
        List<OrderMenuResponse> items
) {

    public static OrderPrepareResponse of(String shopName, String receiver, String phone, String address, String street, List<OrderCouponResponse> coupons, Integer point, long totalPrice, List<OrderMenuResponse> items) {
        return OrderPrepareResponse.builder()
                .shopName(shopName)
                .receiver(receiver)
                .phone(phone)
                .address(address)
                .street(street)
                .coupons(coupons)
                .point(point)
                .totalPrice(totalPrice)
                .items(items)
                .build();
    }
}


