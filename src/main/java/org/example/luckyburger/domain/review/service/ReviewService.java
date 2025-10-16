package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.dto.request.ReviewCreateRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponse createOrderReview(ReviewCreateRequest request) {
        // 1) 주문 존재 확인 - 주문에 대한 정보를 받아 올 수 있을때 활성화
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. id=" + orderId));

        // 2) 엔티티 생성 및 저장 - 변경 예정
        Review review = Review.of(
                request.content(),
                request.rating(),
                request.comment()
        );
        Review saved = reviewRepository.save(review);

        return ReviewResponse.from(saved);
    }
}
