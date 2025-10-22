package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.service.ReviewEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.code.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.ShopNotFoundException;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ShopService {

    private final ShopEntityFinder shopEntityFinder;

    public ShopResponse getShopDetail(String shopName) {

        return ShopResponse.from(shopEntityFinder.getShopByName(shopName));
    }

//    public boolean getCouponListByShopId(Long shopId,Long couponId){
//
//        //해당 couponId를 가진 쿠폰을 추출
//        CouponPolicy couponPolicy = couponPolicyService.findByCouponId(couponId);
//
//        if (couponPolicy.getShop().getId().equals(shopId)){
//
//            Boolean available = couponPolicy.getAvailable();
//
//        }
//        return false;
//    }

}
