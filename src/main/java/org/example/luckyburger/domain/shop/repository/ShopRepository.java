package org.example.luckyburger.domain.shop.repository;

import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Page<Shop> findByNameContaining(String shopName, Pageable pageable);
}
