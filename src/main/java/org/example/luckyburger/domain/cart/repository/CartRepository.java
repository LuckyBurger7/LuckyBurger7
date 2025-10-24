package org.example.luckyburger.domain.cart.repository;

import org.example.luckyburger.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
