package org.example.luckyburger.domain.shop.repository;

import java.util.Optional;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("SELECT s FROM Shop s " +
            "WHERE (:shopName IS NULL OR s.name LIKE CONCAT('%', :shopName, '%'))")
    Page<Shop> findAllByNameContaining(String shopName, Pageable pageable);

    // 더미 데이터 체크
    Optional<Shop> findByNameAndStreet(String name, String street);

}
