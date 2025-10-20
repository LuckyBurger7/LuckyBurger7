package org.example.luckyburger.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.repository.UserCouponRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponUserService {

    private UserCouponRepository userCouponRepository;

    public void createUserCoupon(User user, Coupon coupon) {

    }
}
