package org.example.luckyburger.domain.cart.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.exception.CartMenuNotFoundException;
import org.example.luckyburger.domain.cart.repository.CartMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class CartMenuEntityFinder {

    private final CartMenuRepository cartMenuRepository;

    // cartMenuId로 찾기
    public CartMenu getCartMenuById(Long cartMenuId) {
        return cartMenuRepository.findById(cartMenuId)
                .orElseThrow(CartMenuNotFoundException::new);
    }

    // cartId로 리스트 찾기
    public List<CartMenu> getAllCartMenuByCartId(Long cartId) {
        return cartMenuRepository.findAllByCartId(cartId);
    }
}
