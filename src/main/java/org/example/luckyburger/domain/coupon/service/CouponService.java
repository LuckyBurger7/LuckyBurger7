package org.example.luckyburger.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.dto.response.CouponResponse;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.exception.CouponNotFoundException;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional(readOnly = true)
    public CouponResponse getCouponResponse(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                CouponNotFoundException::new
        );

        return CouponResponse.from(coupon);
    }

    @Transactional(readOnly = true)
    public Page<CouponResponse> getAllCouponResponse(Pageable pageable) {
        Page<Coupon> couponPage = couponRepository.findAll(pageable);

        return couponPage.map(CouponResponse::from);
    }
}
