package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.dto.request.ReviewCreateRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewResponse createUserReview(ReviewCreateRequest request) {
        // 1) 주문 존재 확인
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. id=" + orderId));

        // 2) 엔티티 생성 및 저장
        Review review = Review.of(
                request.content(),
                (int) Math.round(request.rating()), // 엔티티가 int 이므로 보정
                request.comment()
        );
        Review saved = reviewRepository.save(review);

        // 5) 응답 변환
        return new ReviewResponse(
                saved.getId(),
                saved.getRating(),
                saved.getContent(),
                saved.getComment(),
                saved.getCreatedAt(),
                saved.getModifiedAt()
        );
    }
}
