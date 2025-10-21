package org.example.luckyburger.domain.cart.repository;

import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {

    @Query("SELECT DISTINCT cm FROM CartMenu cm " +
            "JOIN FETCH cm.shopMenu sm " +
            "JOIN FETCH sm.shop " +
            "JOIN FETCH sm.menu " +
            "WHERE cm.cart.id = :cartId")
    List<CartMenu> findAllByCartId(Long cartId);
}
