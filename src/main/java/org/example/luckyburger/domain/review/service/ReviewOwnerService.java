package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.dto.request.CommentRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.CommentAlreadyExistsException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewOwnerService {

    private final ReviewRepository reviewRepository;
    private final ReviewEntityFinder reviewEntityFinder;

    /**
     * 점포의 작성된 리뷰 조회 (페이지 당 최대 3개), 사용자 : 점주
     *
     * @param pageable
     * @param shopId
     * @return 작성된 리뷰 반환
     */
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getShopReviewsResponse(Pageable pageable, Long shopId) {

        Pageable limited = PageRequest.of(
                Math.max(0, pageable.getPageNumber()),
                Math.min(pageable.getPageSize(), 3),
                pageable.getSort()
        );
        Page<Review> reviews = reviewRepository.findShopReviews(shopId, limited);
        return reviews.map(ReviewResponse::from);
    }

    /**
     * 작성된 리뷰의 댓글 작성, 사용자 : 점주
     *
     * @param reviewId
     * @param request
     */
    @Transactional
    public void createComment(Long reviewId, CommentRequest request) {
        Review review = reviewEntityFinder.getReviewById(reviewId);

        if (review.getComment() != null && !review.getComment().isEmpty()) {
            throw new CommentAlreadyExistsException();
        }
        review.writeComment(request.comment());
    }
}
