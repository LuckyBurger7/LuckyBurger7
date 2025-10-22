package org.example.luckyburger.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.exception.CouponNotFoundException;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponEntityFinder {

  private final CouponRepository couponRepository;

    public Coupon getCouponById(long id) {
        Coupon coupon= couponRepository.findById(id).orElseThrow(
                CouponNotFoundException::new
        );

        if(coupon.getDeletedAt()!=null){
            throw new CouponNotFoundException();
        }

        return coupon;
    }
}
