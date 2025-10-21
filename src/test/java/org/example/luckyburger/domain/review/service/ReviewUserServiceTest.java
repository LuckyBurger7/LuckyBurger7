package org.example.luckyburger.domain.review.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.dto.request.ReviewRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.ReviewUnauthorizedException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReviewUserService 테스트")
public class ReviewUserServiceTest {

    @InjectMocks
    private ReviewUserService reviewUserService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderEntityFinder orderEntityFinder;

    @Mock
    private ReviewEntityFinder reviewEntityFinder;

    @Test
    @DisplayName("정상적인 리뷰 생성 - 성공")
    void createOrderReview_Success() {
        // given
        Long accountId = 1L;
        Long orderId = 100L;

        AuthAccount authAccount = new AuthAccount(accountId, "user@test.com", AccountRole.ROLE_USER);

        // 리뷰에 대한 소유권과 점포 일치 하는지 검증을 위한 로직
        Account account = mock(Account.class);
        given(account.getId()).willReturn(accountId);

        User user = mock(User.class);
        given(user.getAccount()).willReturn(account);

        Order order = mock(Order.class);
        given(order.getUser()).willReturn(user);

        ReviewRequest request = new ReviewRequest(
                "맛있어요",
                5
        );

        Review savedReview = mock(Review.class);
        given(savedReview.getId()).willReturn(1L);
        given(savedReview.getRating()).willReturn(5.0);
        given(savedReview.getContent()).willReturn("맛있어요");
        given(savedReview.getCreatedAt()).willReturn(LocalDateTime.now());
        given(savedReview.getModifiedAt()).willReturn(LocalDateTime.now());

        given(orderEntityFinder.getOrderById(orderId)).willReturn(order);
        given(reviewRepository.save(any())).willReturn(savedReview);

        // when
        ReviewResponse response = reviewUserService.createOrderReview(authAccount, orderId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.rating()).isEqualTo(5.0);
        assertThat(response.content()).isEqualTo("맛있어요");
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.modifiedAt()).isNotNull();

        verify(orderEntityFinder).getOrderById(orderId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("본인의 주문이 아닌 경우 - 실패")
    void createOrderReview_NotOwner_ThrowsException() {
        // given
        Long accountId = 1L;
        Long otherAccountId = 2L;
        Long orderId = 100L;

        AuthAccount authAccount = new AuthAccount(accountId, "user@test.com", AccountRole.ROLE_USER);

        // 주문을 한 사람이 다른 상황일때 로직
        Account otherAccount = mock(Account.class);
        given(otherAccount.getId()).willReturn(otherAccountId);

        User otherUser = mock(User.class);
        given(otherUser.getAccount()).willReturn(otherAccount);

        Order order = mock(Order.class);
        given(order.getUser()).willReturn(otherUser);

        ReviewRequest request = new ReviewRequest(
                "맛있어요",
                5
        );

        given(orderEntityFinder.getOrderById(orderId)).willReturn(order);

        // when & then
        assertThatThrownBy(() ->
                reviewUserService.createOrderReview(authAccount, orderId, request)
        )
                .isInstanceOf(ReviewUnauthorizedException.class)
                .hasMessage("본인이 작성한 리뷰가 아닙니다.");

        verify(orderEntityFinder).getOrderById(orderId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    @DisplayName("존재하지 않는 주문 ID - 실패")
    void createOrderReview_OrderNotFound_ThrowsException() {
        // given
        Long accountId = 1L;
        Long orderId = 999L;

        AuthAccount authAccount = new AuthAccount(accountId, "user@test.com", AccountRole.ROLE_USER);

        ReviewRequest request = new ReviewRequest(
                "맛있어요",
                5
        );

        given(orderEntityFinder.getOrderById(orderId))
                .willThrow(new IllegalArgumentException("주문을 찾을 수 없습니다."));

        // when & then
        assertThatThrownBy(() ->
                reviewUserService.createOrderReview(authAccount, orderId, request)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 찾을 수 없습니다.");

        verify(orderEntityFinder).getOrderById(orderId);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 조회 - 성공")
    void getOrderReview_Success() {
        // given
        Long accountId = 1L;
        Long reviewId = 10L;

        AuthAccount authAccount = new AuthAccount(accountId, "user@test.com", AccountRole.ROLE_USER);

        Account account = mock(Account.class);
        given(account.getId()).willReturn(accountId);

        User user = mock(User.class);
        given(user.getAccount()).willReturn(account);

        Review review = mock(Review.class);
        given(review.getUser()).willReturn(user);
        given(review.getId()).willReturn(reviewId);
        given(review.getRating()).willReturn(5.0);
        given(review.getContent()).willReturn("맛있어요");
        given(review.getCreatedAt()).willReturn(LocalDateTime.now());
        given(review.getModifiedAt()).willReturn(LocalDateTime.now());

        given(reviewEntityFinder.getReviewById(reviewId)).willReturn(review);

        // when
        ReviewResponse response = reviewUserService.getOrderReview(reviewId, authAccount);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(reviewId);
        assertThat(response.rating()).isEqualTo(5.0);
        assertThat(response.content()).isEqualTo("맛있어요");
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.modifiedAt()).isNotNull();

        verify(reviewEntityFinder).getReviewById(reviewId);
    }

    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void deleteReview_Success() {
        // given
        Long accountId = 1L;
        Long reviewId = 11L;
        AuthAccount auth = new AuthAccount(accountId, "user@test.com", AccountRole.ROLE_USER);

        Account account = mock(Account.class);
        given(account.getId()).willReturn(accountId);

        User user = mock(User.class);
        given(user.getAccount()).willReturn(account);

        Review review = mock(Review.class);
        given(review.getUser()).willReturn(user);

        given(reviewEntityFinder.getReviewById(reviewId)).willReturn(review);

        // when
        reviewUserService.deleteReview(reviewId, auth);

        // then
        verify(reviewRepository, never()).delete(any());
        verify(reviewRepository, never()).deleteById(anyLong());
    }
}