package org.example.luckyburger.domain.shop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.auth.service.OwnerEntityFinder;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.exception.OwnerUnauthorizedAccessException;
import org.example.luckyburger.domain.review.service.ReviewEntityFinder;
import org.example.luckyburger.domain.shop.dto.request.CouponPolicyRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopMenuRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.CouponPolicyResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopDashboardResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopTotalSalesResponse;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.exception.CouponPolicyNotFoundException;
import org.example.luckyburger.domain.shop.exception.OwnerUnauthorizedShopException;
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
    private final ShopCouponRepository shopCouponRepository;
    private final ShopMenuRepository shopMenuRepository;
    private final ReviewEntityFinder reviewEntityFinder;
    private final OwnerEntityFinder ownerEntityFinder;


    // 상점의 쿠폰 사용여부 수정
    @Transactional
    public CouponPolicyResponse updateCouponStatus(Long shopId, Long couponId, CouponPolicyRequest cpr) {

        // 점포와 점주 매칭 검증
        Owner owner = ownerEntityFinder.getOwnerByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());
        if (!owner.getShop().getId().equals(shopId)) {
            throw new OwnerUnauthorizedShopException();
        }

        CouponPolicy couponPolicy = shopCouponRepository.findByShopIdAndCouponId(shopId, couponId).
                orElseThrow(CouponPolicyNotFoundException::new);

        couponPolicy.updateCouponPolicy(cpr.couponStatus());
        return CouponPolicyResponse.from(couponPolicy);
    }

    // 상점의 쿠폰 조회
    @Transactional(readOnly = true)
    public CouponPolicyResponse getCouponPolicyResponse(Long shopId, Long couponId) {
        getOwner(shopId);
        CouponPolicy couponPolicy = shopCouponRepository.findByShopIdAndCouponId(shopId, couponId).
                orElseThrow(CouponPolicyNotFoundException::new);
        return CouponPolicyResponse.from(couponPolicy);
    }

    // 상점의 상태를 변경
    @Transactional
    public ShopResponse updateShopStatus(Long shopId, ShopUpdateRequest request) {
        Shop shopEntity = shopEntityFinder.getShopById(shopId);
        getOwner(shopId);
        shopEntity.updateShopStatus(request.businessStatus());
        return ShopResponse.from(shopEntity);
    }

    // 상점의 메뉴의 상태 변경
    @Transactional
    public ShopMenuResponse updateMenuStatus(Long shopId, Long menuId, ShopMenuRequest request) {
        getOwner(shopId);
        ShopMenu shopMenu = shopMenuRepository.findWithShopByShopIdAndMenuId(shopId, menuId).
                orElseThrow(ShopMenuNotFoundException::new);
        shopMenu.updateShopMenuStatus(request.menuStatus());
        return ShopMenuResponse.from(shopMenu);
    }

    // 월 정산 구간: 매 월 21일 00:00 ~ 다음 달 21일 00:00
    public ShopTotalSalesResponse getTotalSalesByShopIdAndMonth(Long shopId, Integer month) {
        getOwner(shopId);
        ZoneId zone = ZoneId.of("Asia/Seoul");

        Month m = (month == null) ? LocalDate.now(zone).getMonth() : Month.of(month);
        int year = Year.now(zone).getValue();
        YearMonth ym = YearMonth.of(year, m);

        LocalDate startDate = ym.atDay(21);
        LocalDate endDate = ym.plusMonths(1).atDay(21);

        LocalDateTime start = startDate.atStartOfDay(zone).toLocalDateTime();
        LocalDateTime end = endDate.atStartOfDay(zone).toLocalDateTime();
        long total = orderEntityFinder.sumMonthlySalesTotal(shopId, start, end);
        return new ShopTotalSalesResponse(shopId, total);
    }

    @Transactional(readOnly = true)
    public ShopDashboardResponse getShopDashboardByShopId(Long shopId) {
        Shop shop = shopEntityFinder.getShopById(shopId);

        Long todayOrderCount = getTodayOrderCount(shop);
        Long todayTotalSales = getTotalSaleToday(shop);
        Double averageRating = getRatingByShop(shop);

        return ShopDashboardResponse.of(
                shop.getId(),
                todayOrderCount,
                todayTotalSales,
                averageRating
        );
    }

    private Long getTodayOrderCount(Shop shop) {
        LocalDateTime midnightToday = LocalDate.now().atStartOfDay();

        return orderEntityFinder.getCountOrderByShopAndCompletedAndToday(shop, midnightToday);
    }

    private Long getTotalSaleToday(Shop shop) {

        LocalDateTime midnightToday = LocalDate.now().atStartOfDay();

        return orderEntityFinder.getTotalSalesByShopAndToday(shop, midnightToday);

    }

    private Double getRatingByShop(Shop shop) {

        return reviewEntityFinder.getAvgOfRatingByShop(shop);
    }

    @Transactional(readOnly = true)
    public void getOwner(Long shopId) {
        Owner owner = ownerEntityFinder.getOwnerByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());
        if (!owner.getShop().getId().equals(shopId)) {
            throw new OwnerUnauthorizedAccessException();
        }
    }
}
