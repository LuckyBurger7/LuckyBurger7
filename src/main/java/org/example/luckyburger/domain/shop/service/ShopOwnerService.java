package org.example.luckyburger.domain.shop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.service.CouponEntityFinder;
import org.example.luckyburger.domain.menu.service.MenuEntityFinder;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.service.ReviewEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopDashboardResponse;
import org.example.luckyburger.domain.shop.dto.request.CouponPolicyRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopMenuRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.CouponPolicyResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopTotalSalesResponse;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.exception.CouponPolicyNotFoundException;
import org.example.luckyburger.domain.shop.exception.ShopMenuNotFoundException;
import org.example.luckyburger.domain.shop.repository.ShopCouponRepository;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopOwnerService {

    private final ShopEntityFinder shopEntityFinder;
    private final OrderEntityFinder orderEntityFinder;
    private final CouponEntityFinder couponEntityFinder;
    private final ShopCouponRepository shopCouponRepository;
    private final MenuEntityFinder menuEntityFinder;
    private final ShopMenuRepository shopMenuRepository;


    // 상점의 쿠폰 사용여부 수정
    @Transactional
    public CouponPolicyResponse updateCouponStatus(Long shopId, Long couponId, CouponPolicyRequest cpr) {
        // 점포 및 쿠폰 존재 여부 체크
        shopEntityFinder.getShopById(shopId);
        couponEntityFinder.getCouponById(couponId);

        CouponPolicy couponPolicy = shopCouponRepository.findByShopIdAndCouponId(shopId, couponId).
                orElseThrow(CouponPolicyNotFoundException::new);

        couponPolicy.updateCouponPolicy(cpr.couponStatus());
        return CouponPolicyResponse.from(couponPolicy);
    }

    // 상점의 쿠폰 조회
    @Transactional(readOnly = true)
    public CouponPolicyResponse getCouponPolicyResponse(Long shopId, Long couponId) {
        CouponPolicy couponPolicy = shopCouponRepository.findByShopIdAndCouponId(shopId, couponId).
                orElseThrow(CouponPolicyNotFoundException::new);
        return CouponPolicyResponse.from(couponPolicy);
    }

    // 상점의 상태를 변경
    @Transactional
    public ShopResponse updateShopStatus(Long shopId, ShopUpdateRequest request) {
        Shop shopEntity = shopEntityFinder.getShopById(shopId);
        shopEntity.updateShopStatus(request.businessStatus());
        return ShopResponse.from(shopEntity);
    }

    // 상점의 메뉴의 상태 변경
    @Transactional
    public ShopMenuResponse updateMenuStatus(Long shopId, Long menuId, ShopMenuRequest request) {
        shopEntityFinder.getShopById(shopId);
        menuEntityFinder.getMenu(menuId);

        ShopMenu shopMenu = shopMenuRepository.findWithShopByShopIdAndMenuId(shopId, menuId).
                orElseThrow(ShopMenuNotFoundException::new);
        shopMenu.updateShopMenuStatus(request.menuStatus());
        return ShopMenuResponse.from(shopMenu);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderTodayByShop(LocalDateTime start, LocalDateTime end, Long shopId, int page, int size) {

        List<Order> orderList = new ArrayList<>();

        List<Order> orderListByShopId = orderEntityFinder.getAllOrderByShopId(shopId);

        for (Order order : orderListByShopId) {
            if (order.getOrderDate().isBefore(end) && order.getOrderDate().isAfter(start)) {
                orderList.add(order);
            }
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orderPage = new PageImpl<>(orderList, pageable, size);

        return orderPage.map(order -> OrderResponse.of(
                order.getId(),
                order.getShop().getId(),
                order.getReceiver(),
                order.getPhone(),
                order.getAddress(),
                order.getStreet(),
                order.getRequest(),
                order.getCoupon() != null ? order.getCoupon().getId() : null,
                order.getPoint(),
                OrderResponse.Amount.of(order.getTotalPrice(), order.getPay()),
                Collections.emptyList(),
                order.getOrderDate(),
                order.getStatus()
        ));
    }

    @Transactional(readOnly = true)
    public ShopDashboardResponse getShopDashboardByShopId(Long shopId) {

        Shop shop = shopEntityFinder.getShopById(shopId);

        Integer todayOrderCount = getTodayOrderCount(shop);
        Long todayTotalSales = getTotalSaleToday(shop);
        Double averageRating = getRatingByShop(shop);

        return ShopDashboardResponse.of(
                todayOrderCount,
                todayTotalSales,
                averageRating
        );
    }

    private Integer getTodayOrderCount(Shop shop) {
        LocalDateTime midnightToday = LocalDate.now().atStartOfDay();

        return orderEntityFinder.getCountOrderByShopAndToday(shop, midnightToday);
    }

    private Long getTotalSaleToday(Shop shop) {

        LocalDateTime midnightToday = LocalDate.now().atStartOfDay();

        return orderEntityFinder.getTotalSalesByShopAndToday(shop, midnightToday);

    }

    private Double getRatingByShop(Shop shop) {

        return reviewEntityFinder.getAvgOfRatingByShop(shop);
    }
    // 월 정산 구간: 매 월 21일 00:00 ~ 다음 달 21일 00:00
    public ShopTotalSalesResponse getTotalSalesByShopIdAndMonth(Long shopId, Integer month) {
        ZoneId zone = ZoneId.of("Asia/Seoul");

        Month m = (month == null) ? LocalDate.now(zone).getMonth() : Month.of(month);
        int year = Year.now(zone).getValue();
        YearMonth ym = YearMonth.of(year, m);

        LocalDate startDate = ym.atDay(21);
        LocalDate endDate = ym.plusMonths(1).atDay(21);

        LocalDateTime start = startDate.atStartOfDay(zone).toLocalDateTime();
        LocalDateTime end = endDate.atStartOfDay(zone).toLocalDateTime();

        long total = orderEntityFinder.sumMonthlySalesTotal(shopId, start, end);
        return new ShopTotalSalesResponse(total);
    }
}
