package org.example.luckyburger.domain.cart.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.repository.CartMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CartMenuService {

    private final CartMenuRepository cartMenuRepository;
    private final CartMenuEntityFinder cartMenuEntityFinder;

    // CartMenu 삭제
    @Transactional
    public void deleteCartMenu(CartMenu cartMenu) {
        cartMenuRepository.delete(cartMenu);
    }

    // 주문 결제 완료 시 사용할 장바구니 비우기
    @Transactional
    public void clear(Long cartId) {
        cartMenuRepository.deleteAllByCartId(cartId);
    }

    // 총합 금액 계산 (cart Id 기준)
    @Transactional(readOnly = true)
    public long calculateTotalPrice(List<CartMenu> cartMenus) {
        long totalPrice = 0;

        for (CartMenu cartMenu : cartMenus) {
            totalPrice += cartMenu.getShopMenu().getMenu().getPrice() * cartMenu.getQuantity();
        }

        return totalPrice;
    }
}
