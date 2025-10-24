package org.example.luckyburger.domain.review.repository;

import java.util.List;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(
            value = "select r from Review r join fetch r.user u join fetch r.order o where r.shop.id = :shopId",
            countQuery = "select count(r) from Review r where r.shop.id = :shopId")
    Page<Review> findByShop(@Param("shopId") Long shopId, Pageable pageable);

    @Query(
            value = "select r from Review r join fetch r.user u join fetch r.order o where r.shop.id = :shopId and r.deletedAt is null",
            countQuery = "select count(r) from Review r where r.shop.id = :shopId and r.deletedAt is null"
    )
    Page<Review> findByShopNotDeleted(@Param("shopId") Long shopId, Pageable pageable);

    List<Review> findAllByShop(Shop shop);

    Long countByShop(Shop shop);

    @Query("""
                SELECT AVG(r.rating)
                FROM Review r
                WHERE r.shop = :shop
            """)
    Double findAvgOfRatingByShop(Shop shop);

    boolean existsByOrder_IdAndDeletedAtIsNull(Long orderId);


}
