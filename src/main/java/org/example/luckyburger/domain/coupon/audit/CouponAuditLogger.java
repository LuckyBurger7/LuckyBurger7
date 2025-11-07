package org.example.luckyburger.domain.coupon.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponAuditLogger {
    private final CouponAuditRepository couponAuditRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(Long couponId, Long userId, CouponIssueStatus status, String reason) {
        try {
            couponAuditRepository.save(CouponAudit.of(couponId, userId, status, reason));
        } catch (Exception ignored) {
        }
    }
}
