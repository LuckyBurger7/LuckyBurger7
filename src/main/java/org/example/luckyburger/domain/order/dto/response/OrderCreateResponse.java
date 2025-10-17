package org.example.luckyburger.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.example.luckyburger.domain.order.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderCreateResponse(
        Long orderId,
        Long shopId,
        String receiver,
        String phone,
        String address,
        String street,
        String request,
        Long couponId,
        Integer point,
        Integer addedPoint,
        Amount amount,
        List<OrderMenuResponse> items,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime orderDate,
        OrderStatus status
) {
    public static OrderCreateResponse of(Long orderId, Long shopId, String receiver, String phone, String address, String street, String request, Long couponId, Integer point, Integer addedPoint, Amount amount, List<OrderMenuResponse> items, LocalDateTime orderDate, OrderStatus status) {
        return OrderCreateResponse.builder()
                .orderId(orderId)
                .shopId(shopId)
                .receiver(receiver)
                .phone(phone)
                .address(address)
                .street(street)
                .request(request)
                .couponId(couponId)
                .point(point)
                .addedPoint(addedPoint)
                .amount(amount)
                .items(items)
                .orderDate(orderDate)
                .status(status)
                .build();
    }

    @Builder
    public record Amount(long subtotal, long discount, long pay) {
        public static Amount of(long subtotal, long discount, long pay) {
            return Amount.builder()
                    .subtotal(subtotal)
                    .discount(discount)
                    .pay(pay)
                    .build();
        }
    }
}


