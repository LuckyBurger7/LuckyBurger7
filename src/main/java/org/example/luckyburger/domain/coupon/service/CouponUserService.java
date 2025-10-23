package org.example.luckyburger.domain.coupon.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.coupon.dto.response.UserCouponResponse;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.exception.CouponOutOfStockException;
import org.example.luckyburger.domain.coupon.exception.DuplicateUserCouponException;
import org.example.luckyburger.domain.coupon.repository.UserCouponRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponUserService {

    private final UserCouponRepository userCouponRepository;
    private final CouponEntityFinder couponEntityFinder;
    private final UserEntityFinder userEntityFinder;

    @Transactional
    public UserCouponResponse issueCoupon(Long couponId) {

        Coupon coupon = couponEntityFinder.getCouponById(couponId);
        // 쿠폰 갯수 검사
        if (coupon.getCount() <= 0)
            throw new CouponOutOfStockException();

        coupon.issueCoupon();

        User user = userEntityFinder.getUserByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());

        // 쿠폰 중복 검사
        if (userCouponRepository.existsByUserAndCoupon(user, coupon))
            throw new DuplicateUserCouponException();

        UserCoupon userCoupon = UserCoupon.of(
                user,
                coupon,
                LocalDateTime.now()
        );

        return UserCouponResponse.from(userCouponRepository.save(userCoupon));
    }

    @Transactional(readOnly = true)
    public Page<UserCouponResponse> getAllVerifiedUserCouponResponse(Pageable pageable) {

        User user = userEntityFinder.getUserByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());

        Page<UserCoupon> userCouponPage = userCouponRepository.findAllByUser(user, pageable);

        return userCouponPage.map(UserCouponResponse::from);
    }
}
