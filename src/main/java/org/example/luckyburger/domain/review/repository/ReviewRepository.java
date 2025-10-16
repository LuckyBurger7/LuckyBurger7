package org.example.luckyburger.domain.review.repository;

import org.example.luckyburger.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH User u on u = r.user JOIN FETCH Shop s on s = r.shop")
    Page<Review> findReviewPage(Pageable pageable);
}
