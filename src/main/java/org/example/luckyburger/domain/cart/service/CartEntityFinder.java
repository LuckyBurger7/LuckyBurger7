package org.example.luckyburger.domain.cart.service;

import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CartEntityFinder {

    // TODO: OrElseThrow NOTFOUND
    public Cart getCartByUserId(@NotNull Long userId) {
        return null;
    }
}
