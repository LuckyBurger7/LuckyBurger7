package org.example.luckyburger.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponUserService {

    private CouponRepository couponRepository;


}
