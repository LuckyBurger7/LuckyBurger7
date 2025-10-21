package org.example.luckyburger.domain.shop.repository;

import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByName(String shopName);

    Optional<Void> deleteByShopId(Long shopId);

}
