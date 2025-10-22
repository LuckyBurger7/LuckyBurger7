package org.example.luckyburger.domain.shop.repository;

import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("SELECT s FROM Shop s " +
            "WHERE (:shopname IS NULL OR s.name LIKE CONCAT('%', :shopname, '%'))")
    Page<Shop> findAllByNameContaining(String shopName, Pageable pageable);
}
