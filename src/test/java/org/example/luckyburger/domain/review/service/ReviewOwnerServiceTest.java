package org.example.luckyburger.domain.review.service;

import org.example.luckyburger.domain.review.dto.request.CommentRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewOwnerService 테스트")
public class ReviewOwnerServiceTest {

    @InjectMocks
    private ReviewOwnerService reviewOwnerService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewEntityFinder reviewEntityFinder;

    @Test
    @DisplayName("점포에 작성된 리뷰를 모두 조회 - 성공")
    void getShopReview_Success() {
        // given
        Long shopId = 100L;
        Pageable input = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));

        Review r1 = mock(Review.class);
        given(r1.getId()).willReturn(1L);
        given(r1.getContent()).willReturn("맛있어요!");
        given(r1.getRating()).willReturn(5.0);
        given(r1.getComment()).willReturn(null);
        given(r1.getCreatedAt()).willReturn(LocalDateTime.now());
        given(r1.getModifiedAt()).willReturn(LocalDateTime.now());

        Review r2 = mock(Review.class);
        given(r2.getId()).willReturn(2L);
        given(r2.getContent()).willReturn("또 오고 싶어요");
        given(r2.getRating()).willReturn(4.0);
        given(r2.getComment()).willReturn("사장님 댓글 감사합니다");
        given(r2.getCreatedAt()).willReturn(LocalDateTime.now());
        given(r2.getModifiedAt()).willReturn(LocalDateTime.now());

        given(reviewRepository.findByShop(eq(shopId), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(r1, r2), PageRequest.of(0, 3), 2));

        // when
        Page<ReviewResponse> page = reviewOwnerService.getShopReviewsResponse(input, shopId);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).id()).isEqualTo(1L);
        assertThat(page.getContent().get(0).content()).isEqualTo("맛있어요!");
        assertThat(page.getContent().get(0).rating()).isEqualTo(5.0);
        assertThat(page.getContent().get(0).comment()).isNull();
        assertThat(page.getContent().get(1).id()).isEqualTo(2L);
        assertThat(page.getContent().get(1).content()).isEqualTo("또 오고 싶어요");
        assertThat(page.getContent().get(1).rating()).isEqualTo(4.0);
        assertThat(page.getContent().get(1).comment()).isEqualTo("사장님 댓글 감사합니다");

        verify(reviewRepository).findByShop(eq(shopId), any(Pageable.class));
    }

    @Test
    @DisplayName("리뷰 댓글 작성 - 성공")
    void createComment_success_whenNoExistingComment() {
        // given
        Long reviewId = 1L;
        String newComment = "이용해 주셔서 감사합니다";
        CommentRequest request = new CommentRequest(newComment);

        Review review = mock(Review.class);
        given(review.getComment()).willReturn(null);
        given(reviewEntityFinder.getReviewById(reviewId)).willReturn(review);

        // when
        assertThatCode(() -> reviewOwnerService.createComment(reviewId, request))
                .doesNotThrowAnyException();

        // then
        verify(reviewEntityFinder).getReviewById(reviewId);
        verify(review).writeComment(newComment);
        verifyNoMoreInteractions(reviewEntityFinder, review);
    }
}
