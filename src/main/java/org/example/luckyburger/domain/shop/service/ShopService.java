package org.example.luckyburger.domain.shop.service;

import lombok.AllArgsConstructor;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.service.ReviewEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.exception.shopCode.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.shopCode.ShopException;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final OrderEntityFinder orderEntityFinder;
    private final ShopEntityFinder shopEntityFinder;
    private final ReviewEntityFinder reviewEntityFinder;

    @Transactional(readOnly = true)
    public ShopResponse getShopDetail(String shopName) {

        return ShopResponse.from(shopEntityFinder.getShopByName(shopName));
    }


    @Transactional(readOnly = true)
    public Long countShop(){

        Long count = shopRepository.count();

        return count;
    }

    @Transactional(readOnly = true)
    public Shop entityFinder(Long shopId){
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

        return shop;
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
