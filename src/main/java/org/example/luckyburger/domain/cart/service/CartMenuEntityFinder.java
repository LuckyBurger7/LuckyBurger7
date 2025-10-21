package org.example.luckyburger.domain.cart.service;

import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CartMenuEntityFinder {

    // TODO:
    public List<CartMenu> getByCartId(Long cartId) {
        return null;
    }
}
