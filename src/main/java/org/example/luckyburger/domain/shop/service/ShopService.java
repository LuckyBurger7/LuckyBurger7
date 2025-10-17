package org.example.luckyburger.domain.shop.service;

import lombok.AllArgsConstructor;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderService;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.shop.dto.request.CreateShopRequest;
import org.example.luckyburger.domain.shop.dto.request.UpdateShopRequest;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.exception.shop.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.shop.ShopException;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final OrderService orderService;

    public Shop createShop(CreateShopRequest shopRequest) {

        Shop shop = Shop.createFrom(shopRequest);

        shopRepository.save(shop);

        System.out.println(shop);

        return shop;
    }

    public Shop findShopDetail(String shopName) {

        return shopRepository.findByName(shopName);
    }

    // 상점의 상태를 변경
    @Transactional
    public Shop changeStatus(Long shopId,
                             BusinessStatus shopStatus) {

        Shop shop = entityFinder(shopId);
        shop.changeShop(shopStatus);

        return shop;
    }

    // 상점의 정보를 수정
    @Transactional
    public Shop updateShop(Long shopId, UpdateShopRequest updateShopRequest) {

        Shop shop = entityFinder(shopId);

        Shop updatedShop = shop.updateFrom(updateShopRequest);

        return updatedShop;
    }

    // 상점 삭제
    public void deleteShop(Long shopId) {

        shopRepository.deleteById(shopId);
    }

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

    public int getTotal(Long shopId){

        int totalPrice = 0;

        List<Order> orderListByShop = orderService.getTotal(shopId);

        for (Order order : orderListByShop) {
            totalPrice += order.getTotalPrice();
        }

        return totalPrice;
    }

    public List<Order> getOrderTodayByShop(LocalDateTime localDateTime, Long shopId){

        List<Order> orderList = new ArrayList<>();

        List<Order> orderListByShopId = orderService.getTotal(shopId);

        for (Order order : orderListByShopId) {
            if (order.getOrderDate().equals(localDateTime)){
                orderList.add(order);
            }
        }

        return orderList;
    }

    public int getTotalsaleToday(Long shopId,LocalDateTime localDateTime) {

        List<Order> orderTodayByShop = getOrderTodayByShop(localDateTime, shopId);

        int totalPrice = 0;

        List<Order> orderListByShop = orderService.getTotal(shopId);

        for (Order order : orderListByShop) {
            totalPrice += order.getTotalPrice();
        }

        return totalPrice;

    }

    public int getTotalSaleByMonthWithShopId(Long shopId,LocalDateTime localDateTime){

        int totalSaleByMonth = 0;

        List<Order> orderListByShopId = orderService.getTotal(shopId);

        for (Order order : orderListByShopId) {
            if (order.getOrderDate().equals(localDateTime)){
                totalSaleByMonth += (int) order.getTotalPrice();
            }
        }

        return totalSaleByMonth;
    }

    public int getRatingByShop(Long shopId){

        int totalRating = 0;
        int count = 0;

        List<Review> reviewList = reviewService.getReviewListByShopId(shopId);

        for (Review review : reviewList) {
            totalRating += review.getRating();
            count++;
        }

        return totalRating/count;
    }

    public boolean getCouponListByShopId(Long shopId,Long couponId){

        //해당 couponId를 가진 쿠폰을 추출
        CouponPolicy couponPolicy = couponPolicyService.findByCouponId(couponId);

        if (couponPolicy.getShop().getId().equals(shopId)){

            Boolean available = couponPolicy.getAvailable();

        }
        return false;
    }

}
