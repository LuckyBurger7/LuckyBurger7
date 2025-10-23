package org.example.luckyburger.domain.review.repository;

import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(
            value = "select r from Review r join fetch r.user u join fetch r.order o where r.shop.id = :shopId",
            countQuery = "select count(r) from Review r where r.shop.id = :shopId")
    Page<Review> findShopReviews(@Param("shopId") Long shopId, Pageable pageable);

    @Query(
            value = "select r from Review r join fetch r.user u join fetch r.order o where r.shop.id = :shopId and r.deletedAt is null",
            countQuery = "select count(r) from Review r where r.shop.id = :shopId and r.deletedAt is null"
    )
    Page<Review> findShopReviewsNotDeleted(@Param("shopId") Long shopId, Pageable pageable);

    List<Review> findAllByShop(Shop shop);

    List<Review> shop(Shop shop);

    Long countByShop(Shop shop);

    @Query("""
                SELECT SUM(r.rating)
                FROM Review r
                WHERE r.shop = :shop
            """)
    Double findSumOfRatingByShop(Shop shop);
}
