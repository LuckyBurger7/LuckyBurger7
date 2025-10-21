package org.example.luckyburger.domain.cart.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CartResponse(Long cartId, List<CartMenuResponse> cartMenus, long totalPrice) {

    public static CartResponse of(Cart cart, List<CartMenu> cartMenus) {
        List<CartMenuResponse> menuResponses = cartMenus.stream()
                .map(CartMenuResponse::from)
                .toList();

        return CartResponse.builder()
                .cartId(cart.getId())
                .cartMenus(menuResponses)
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
