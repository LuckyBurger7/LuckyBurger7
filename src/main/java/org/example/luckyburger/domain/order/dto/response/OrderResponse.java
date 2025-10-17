package org.example.luckyburger.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        Long orderId,
        Long shopId,
        String receiver,
        String phone,
        String address,
        String street,
        String request,
        Long couponId,
        Integer point,
        Amount amount,
        List<OrderMenuResponse> items,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime orderDate,
        OrderStatus status
) {

    public static OrderResponse from(Order order, List<OrderMenuResponse> items) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .shopId(order.getShop().getId())
                .receiver(order.getReceiver())
                .phone(order.getPhone())
                .address(order.getAddress())
                .street(order.getStreet())
                .request(order.getRequest())
                .couponId(order.getCoupon() != null ? order.getCoupon().getId() : null)
                .point(order.getPoint())
                .amount(OrderResponse.Amount.of(
                        order.getTotalPrice(),
                        order.getPay()))
                .items(items)
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .build();
    }

    @Builder
    public record Amount(long subtotal, long pay) {
        public static Amount of(long subtotal, long pay) {
            return Amount.builder()
                    .subtotal(subtotal)
                    .pay(pay)
                    .build();
        }
    }
}


