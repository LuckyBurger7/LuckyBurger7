package org.example.luckyburger.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.dto.response.CouponResponse;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponAdminService {

    private final CouponRepository couponRepository;
    private final CouponEntityFinder couponEntityFinder;

    @Transactional
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

    @Transactional
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

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponEntityFinder.getCouponById(couponId);

        coupon.delete();
    }

    /**
     * 현재일 기준으로 활성화된 쿠폰들을 모두 가져온다.
     *
     * @param pageable 페이지 설정
     * @return 쿠폰 응답 DTO 페이지
     */
    @Transactional(readOnly = true)
    public Page<CouponResponse> getAllAvailableCoupon(Pageable pageable) {
        Page<Coupon> couponPage = couponRepository.findCouponsByExpirationDateAfter(LocalDateTime.now(), pageable);

        return couponPage.map(CouponResponse::from);
    }
}
