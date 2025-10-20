package org.example.luckyburger.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.dto.response.CouponResponse;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponAdminService {

    private final CouponRepository couponRepository;
    private final CouponEntityFinder couponEntityFinder;

    public CouponResponse createCoupon(CouponRequest couponRequest) {

        Coupon coupon = Coupon.of(
                couponRequest.name(),
                couponRequest.discount(),
                couponRequest.count(),
                couponRequest.expirationDate(),
                couponRequest.type()
        );

        return CouponResponse.from(couponRepository.save(coupon));
    }

    public CouponResponse updateCoupon(Long couponId, CouponRequest couponRequest) {

        Coupon coupon = couponEntityFinder.getCouponById(couponId);

        coupon.updateCoupon(
                couponRequest.name(),
                couponRequest.discount(),
                couponRequest.count(),
                couponRequest.expirationDate(),
                couponRequest.type()
        );

        return CouponResponse.from(coupon);
    }

    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponEntityFinder.getCouponById(couponId);

        coupon.delete();
    }

    public Page<CouponResponse> getAllCouponByAvailable(Pageable pageable) {
        Page<Coupon> couponPage = couponRepository.findCouponsByExpirationDateBefore(LocalDateTime.now(), pageable);

        return couponPage.map(CouponResponse::from);
    }
}
