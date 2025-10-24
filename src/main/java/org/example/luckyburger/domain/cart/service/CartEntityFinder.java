package org.example.luckyburger.domain.cart.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.exception.CartNotFoundException;
import org.example.luckyburger.domain.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class CartEntityFinder {

    private final CartRepository cartRepository;

    // userId로 찾기
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findById(userId)
                .orElseThrow(CartNotFoundException::new);
    }
}
