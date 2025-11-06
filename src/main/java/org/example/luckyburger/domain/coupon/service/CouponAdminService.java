package org.example.luckyburger.domain.coupon.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.dto.response.CouponResponse;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.exception.CouponCountLessThanIssuedException;
import org.example.luckyburger.domain.coupon.redis.CouponKeys;
import org.example.luckyburger.domain.coupon.redis.CouponScripts;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.example.luckyburger.domain.shop.enums.CouponStatus;
import org.example.luckyburger.domain.shop.repository.ShopCouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponAdminService {

    private final RedisTemplate<String, String> redis;

    private final CouponRepository couponRepository;
    private final CouponEntityFinder couponEntityFinder;
    private final ShopCouponRepository shopCouponRepository;
    private final CouponScripts couponScripts;

    @Transactional
    public CouponResponse createCouponWithRedis(CouponRequest couponRequest) {

        Coupon coupon = Coupon.of(
                couponRequest.name(),
                couponRequest.discount(),
                couponRequest.count(),
                couponRequest.expirationDate(),
                couponRequest.type()
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        // 쿠폰 초기 상태 UNAVAILABLE
        shopCouponRepository.saveForAllShop(savedCoupon.getId(), CouponStatus.UNAVAILABLE.name());

        // Redis 재고 및 gate 설정 (expireAt 만료일+1)
        Instant expireAt = couponRequest.expirationDate().atZone(ZoneId.systemDefault()).toInstant().plus(Duration.ofDays(1));
        couponScripts.setStock(savedCoupon.getId(), savedCoupon.getCount(), expireAt);
        Instant openAt = couponRequest.openDate().atZone(ZoneId.systemDefault()).toInstant();
        couponScripts.setGate(savedCoupon.getId(), openAt);

        couponScripts.setIssuedSet(savedCoupon.getId(), expireAt);

        return CouponResponse.from(savedCoupon);
    }

    @Transactional
    public CouponResponse updateCouponWithRedis(Long couponId, CouponRequest couponRequest) {

        Coupon coupon = couponEntityFinder.getCouponById(couponId);

        coupon.updateCoupon(
                couponRequest.name(),
                couponRequest.discount(),
                couponRequest.count(),
                couponRequest.expirationDate(),
                couponRequest.type()
        );

        long issuedCount = Optional.ofNullable(
                redis.opsForSet().size(CouponKeys.issuedSetKey(couponId))
        ).orElse(0L);
        long newTotal = couponRequest.count();

        if (newTotal < issuedCount) {
            throw new CouponCountLessThanIssuedException();
        }

        long remain = newTotal - issuedCount;
        Instant expireAt = couponRequest.expirationDate().atZone(ZoneId.systemDefault()).toInstant().plus(Duration.ofDays(1));
        couponScripts.setStock(coupon.getId(), remain, expireAt);

        Instant openAt = couponRequest.openDate().atZone(ZoneId.systemDefault()).toInstant();
        couponScripts.setGate(coupon.getId(), openAt);

        couponScripts.setIssuedSet(coupon.getId(), expireAt);

        return CouponResponse.from(coupon);
    }

    @Transactional
    public void deleteCouponWithRedis(Long couponId) {
        Coupon coupon = couponEntityFinder.getCouponById(couponId);

        couponScripts.deleteAllKey(coupon.getId());
        shopCouponRepository.updateStatusByCouponId(coupon.getId(), CouponStatus.UNAVAILABLE);
        coupon.delete();
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest couponRequest) {

        Coupon coupon = Coupon.of(
                couponRequest.name(),
                couponRequest.discount(),
                couponRequest.count(),
                couponRequest.expirationDate(),
                couponRequest.type()
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        // 쿠폰 초기 상태 UNAVAILABLE
        shopCouponRepository.saveForAllShop(savedCoupon.getId(), CouponStatus.UNAVAILABLE.name());

        return CouponResponse.from(savedCoupon);
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

        String stockKey = "stock:coupon:" + coupon.getId();
        redis.opsForValue().set(stockKey, String.valueOf(coupon.getCount()));

        String gateKey = "gate:coupon:" + coupon.getId();

        LocalDateTime openDate = couponRequest.openDate();
        Instant openAt = openDate.atZone(ZoneId.systemDefault()).toInstant();
        Instant now = Instant.now();

        if (openAt.isAfter(now)) {
            redis.execute((RedisConnection conn) -> {
                byte[] keyBytes = gateKey.getBytes(StandardCharsets.UTF_8);
                conn.stringCommands().set(keyBytes, "1".getBytes(StandardCharsets.UTF_8));
                conn.keyCommands().pExpireAt(keyBytes, openAt.toEpochMilli());
                return null;
            });
        }

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
