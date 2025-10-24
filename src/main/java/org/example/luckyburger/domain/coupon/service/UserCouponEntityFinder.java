package org.example.luckyburger.domain.coupon.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.exception.CouponExpiredException;
import org.example.luckyburger.domain.coupon.exception.UserCouponNotFoundException;
import org.example.luckyburger.domain.coupon.repository.UserCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class UserCouponEntityFinder {

    private final UserCouponRepository userCouponRepository;

    /**
     * 로그인한 유저의 보유 쿠폰을 반환
     *
     * @param couponId 쿠폰 id
     * @return 유저 쿠폰 엔티티 반환
     */
    public UserCoupon getVerifiedUserCouponByCouponId(Long couponId) {

        UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(AuthAccountUtil.getAuthAccount().getAccountId(), couponId).orElseThrow(
                UserCouponNotFoundException::new
        );

        if (userCoupon.getCoupon().getDeletedAt() != null // 쿠폰이 삭제됐는지
                || userCoupon.getCoupon().isExpired() // 쿠폰이 만료됐는지
                || userCoupon.getUsedDate() != null // 쿠폰이 사용됐는지
        ) {
            throw new CouponExpiredException();
        }

        return userCoupon;
    }

    public UserCoupon getUserCouponByCouponId(Long couponId) {

        return userCouponRepository.findByUserIdAndCouponId(AuthAccountUtil.getAuthAccount().getAccountId(), couponId).orElseThrow(
                UserCouponNotFoundException::new
        );
    }

    public List<UserCoupon> getAllVerifiedUserCouponByUserId(Long userId) {
        return userCouponRepository.findAvailableByUserId(userId);
    }
}
