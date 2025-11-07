package org.example.luckyburger.domain.coupon.service;

import jakarta.persistence.QueryTimeoutException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.coupon.audit.CouponAuditLogger;
import org.example.luckyburger.domain.coupon.audit.CouponIssueStatus;
import org.example.luckyburger.domain.coupon.dto.response.UserCouponResponse;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.exception.CouponNotIssuedException;
import org.example.luckyburger.domain.coupon.exception.CouponNotOpenedException;
import org.example.luckyburger.domain.coupon.exception.CouponOutOfStockException;
import org.example.luckyburger.domain.coupon.exception.DuplicateUserCouponException;
import org.example.luckyburger.domain.coupon.redis.CouponScripts;
import org.example.luckyburger.domain.coupon.repository.UserCouponRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponUserService {

    private final UserCouponRepository userCouponRepository;
    private final CouponEntityFinder couponEntityFinder;
    private final UserEntityFinder userEntityFinder;
    private final CouponScripts couponScripts;
    private final CouponIssueTx couponIssueTx;
    private final CouponAuditLogger audit;

    private final long RESERVE_TTL = 15_000L;
    private final int THREAD_SLEEP = 100;

    public UserCouponResponse issueCouponWithRedis(Long couponId) {
        User user = userEntityFinder.getUserByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());
        Long userId = user.getId();

        Long result = couponScripts.reserveAndDecr(couponId, userId, RESERVE_TTL);

        // fail-fast
        if (result == null || result == -1L) {
            // 오픈 전
            audit.log(couponId, userId, CouponIssueStatus.NOT_OPENED, null);
            throw new CouponNotOpenedException();
        }
        if (result == 0L) {
            // 매진
            audit.log(couponId, userId, CouponIssueStatus.SOLD_OUT, null);
            throw new CouponOutOfStockException();
        }
        if (result == 2L) {
            // 이미 발급
            audit.log(couponId, userId, CouponIssueStatus.ALREADY_ISSUED, null);
            throw new DuplicateUserCouponException();
        }

        audit.log(couponId, userId, CouponIssueStatus.RESERVED, null);

        int attempts = 0;
        while (true) {
            try {
                UserCoupon saved = couponIssueTx.persistOnce(couponId, user);
                audit.log(couponId, userId, CouponIssueStatus.ISSUED, null);
                couponScripts.confirmIssued(couponId, userId);
                return UserCouponResponse.from(saved);

            } catch (DataIntegrityViolationException e) {
                couponScripts.compensateIfReserved(couponId, userId);
                audit.log(couponId, userId, CouponIssueStatus.DUPLICATE_DB, null);
                audit.log(couponId, userId, CouponIssueStatus.COMPENSATE, null);
                throw new DuplicateUserCouponException();
            } catch (TransientDataAccessException | QueryTimeoutException tx) {
                if (++attempts > 2) {
                    couponScripts.compensateIfReserved(couponId, userId);
                    audit.log(couponId, userId, CouponIssueStatus.RETRY_FAIL, null);
                    audit.log(couponId, userId, CouponIssueStatus.COMPENSATE, null);
                    throw new CouponNotIssuedException();
                }
                try {
                    Thread.sleep(THREAD_SLEEP + ThreadLocalRandom.current().nextInt(THREAD_SLEEP));
                } catch (InterruptedException ignored) {
                }
            } catch (RuntimeException e) {
                couponScripts.compensateIfReserved(couponId, userId);
                audit.log(couponId, userId, CouponIssueStatus.UNKNOWN_ERROR, null);
                audit.log(couponId, userId, CouponIssueStatus.COMPENSATE, null);
                throw new CouponNotIssuedException();
            }
        }
    }

    @Transactional
    public UserCouponResponse issueCoupon(Long couponId) {

        Coupon coupon = couponEntityFinder.getCouponById(couponId);
        // 쿠폰 갯수 검사
        if (coupon.getCount() <= 0)
            throw new CouponOutOfStockException();

        coupon.issueCoupon();

        User user = userEntityFinder.getUserByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());
        ;

        // 쿠폰 중복 검사
        if (userCouponRepository.existsByUserAndCoupon(user, coupon))
            throw new DuplicateUserCouponException();

        UserCoupon userCoupon = UserCoupon.of(
                user,
                coupon,
                LocalDateTime.now()
        );

        return UserCouponResponse.from(userCouponRepository.save(userCoupon));
    }

    @Transactional(readOnly = true)
    public Page<UserCouponResponse> getAllVerifiedUserCouponResponse(Pageable pageable) {

        Page<UserCoupon> userCouponPage = userCouponRepository.findAllByUserId(AuthAccountUtil.getAuthAccount().getAccountId(), pageable);

        return userCouponPage.map(UserCouponResponse::from);
    }
}
