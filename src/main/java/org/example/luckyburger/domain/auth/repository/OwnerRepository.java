package org.example.luckyburger.domain.auth.repository;

import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @EntityGraph(attributePaths = {"shop"})
    Optional<Owner> findByAccount(Account account);

    boolean existsOwnerByShop(Shop shop);

    @EntityGraph(attributePaths = {"shop"})
    Page<Owner> findAllByShopNotNull(Pageable pageable);
}
