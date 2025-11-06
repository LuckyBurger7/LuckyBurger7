package org.example.luckyburger.domain.cart.repository;

import jakarta.websocket.server.PathParam;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {

    @Query("SELECT DISTINCT cm FROM CartMenu cm " +
            "JOIN FETCH cm.shopMenu sm " +
            "JOIN FETCH sm.shop " +
            "JOIN FETCH sm.menu " +
            "WHERE cm.cart.id = :cartId")
    List<CartMenu> findAllByCartId(Long cartId);

    @Query("""
            SELECT cm
            FROM CartMenu cm
            JOIN FETCH cm.cart c
            JOIN FETCH cm.shopMenu sm
            WHERE c.id = :cartId AND sm.id = :shopMenuId
            """)
    Optional<CartMenu> findByCartIdAndShopMenuId(@PathParam("cartId") Long cartId, @PathParam("shopMenuId") Long shopMenuId);

    void deleteAllByCartId(Long cartId);
}
