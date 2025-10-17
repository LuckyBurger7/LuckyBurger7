package org.example.luckyburger.domain.review.repository;

import org.example.luckyburger.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query(
            value = "select r from Review r join fetch r.user u join fetch r.order o where r.shop.id = :shopId",
            countQuery = "select count(r) from Review r where r.shop.id = :shopId")
    Page<Review> findShopReviews(@Param("shopId") Long shopId, Pageable pageable);
}
