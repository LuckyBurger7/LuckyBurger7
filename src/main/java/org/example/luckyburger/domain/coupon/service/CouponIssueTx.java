package org.example.luckyburger.domain.coupon.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.repository.UserCouponRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssueTx {

    private final UserCouponRepository userCouponRepository;
    private final CouponEntityFinder couponEntityFinder;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected UserCoupon persistOnce(Long couponId, User user) {
        Coupon coupon = couponEntityFinder.getCouponById(couponId);

        UserCoupon userCoupon = UserCoupon.of(
                user,
                coupon,
                LocalDateTime.now()
        );

        return userCouponRepository.save(userCoupon);
    }
}
