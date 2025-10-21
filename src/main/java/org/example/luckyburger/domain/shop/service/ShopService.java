package org.example.luckyburger.domain.shop.service;

import lombok.AllArgsConstructor;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.service.ReviewEntityFinder;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.exception.shop.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.shop.ShopException;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final OrderEntityFinder orderEntityFinder;
    private final ShopEntityFinder shopEntityFinder;
    private final ReviewEntityFinder reviewEntityFinder;

    @Transactional
    public ShopResponse createShop(ShopRequest shopRequest) {

        Shop shopEntity = Shop.of(shopRequest.getName(), shopRequest.getStatus(), shopRequest.getAddress(),shopRequest.getStreet());

        shopRepository.save(shopEntity);

        return ShopResponse.from(shopEntity);

    }

    @Transactional(readOnly = true)
    public ShopResponse getShopDetail(String shopName) {

        Shop shop = shopRepository.findByName(shopName)
                .orElseThrow(() -> new ShopException(ShopErrorCode.SHOP_NOT_FOUND));

        return ShopResponse.from(shop);
    }

    // 상점의 상태를 변경
    @Transactional
    public Shop updateStatus(Long shopId,
                             BusinessStatus shopStatus) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        shopEntity.changeShop(shopStatus);

        return shopEntity;
    }

    // 상점의 정보를 수정
    @Transactional
    public ShopResponse updateShop(Long shopId, ShopRequest shopRequest) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        Shop updatedShop = shopEntity.updateOf(shopRequest.getName(),
                shopRequest.getStatus(),
                shopRequest.getAddress(),
                shopRequest.getStreet());

        shopRepository.save(updatedShop);

        return ShopResponse.from(updatedShop);
    }

    // 상점 삭제
    @Transactional
    public void deleteShop(Long shopId) {

        shopRepository.deleteById(shopId);
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

    //점포 총 매출 조회
    @Transactional(readOnly = true)
    public int getTotal(Long shopId){

        int totalPrice = 0;

        List<Order> orderListByShop = orderEntityFinder.getAllOrderByShopId(shopId);

        for (Order order : orderListByShop) {
            totalPrice += order.getTotalPrice();
        }

        return totalPrice;
    }

    @Transactional(readOnly = true)
    public List<Order> getOrderTodayByShop(LocalDateTime start, LocalDateTime end,Long shopId){

//        List<Order> orderList = new ArrayList<>();

        List<Order> orderListByShopId = orderEntityFinder.getAllOrderByShopId(shopId);

//        for (Order order : orderListByShopId) {
//            if (order.getOrderDate().isBefore(end) && order.getOrderDate().isAfter(start)) {
//                orderList.add(order);
//            }
//        }

        return orderListByShopId.stream()
                .filter(order -> order.getOrderDate().isAfter(start) && order.getOrderDate().isBefore(end))
                .map(order -> {
                    // OrderMenuResponse로 변환
                    List<OrderMenuResponse> items = order.getOrderMenus().stream()
                            .map(orderMenu -> OrderMenuResponse.from(orderMenu))
                            .collect(Collectors.toList());

                    return OrderResponse.from(order, items);
                })
                .collect(Collectors.toList());

//        return orderList;
    }

//    @Transactional(readOnly = true)
//    public int getTotalsaleToday(Long shopId,LocalDateTime localDateTime) {
//
//        List<Order> orderTodayByShop = getOrderTodayByShop(localDateTime, shopId);
//
//        int totalPrice = 0;
//
//        List<Order> orderListByShop = orderService.getTotal(shopId);
//
//        for (Order order : orderListByShop) {
//            totalPrice += order.getTotalPrice();
//        }
//
//        return totalPrice;
//
//    }
//
//    @Transactional(readOnly = true)
//    public int getTotalSaleByMonthWithShopId(Long shopId,LocalDateTime localDateTime){
//
//        int totalSaleByMonth = 0;
//
//        List<Order> orderListByShopId = orderService.getTotal(shopId);
//
//        for (Order order : orderListByShopId) {
//            if (order.getOrderDate().equals(localDateTime)){
//                totalSaleByMonth += (int) order.getTotalPrice();
//            }
//        }
//
//        return totalSaleByMonth;
//    }

    public double getRatingByShop(Long shopId){

        double totalRating = 0;
        double count = 0;

        List<Review> reviewList = reviewEntityFinder.getReviewListByShopId(shopId);

        for (Review review : reviewList) {
            totalRating += review.getRating();
            count++;
        }

        double shopRating = totalRating/count;

        return shopRating;
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
