package org.example.luckyburger.domain.coupon.audit;

public enum CouponIssueStatus {
    ISSUED,
    RESERVED,

    ALREADY_ISSUED,
    DUPLICATE_DB,

    NOT_OPENED,
    SOLD_OUT,
    RESERVE_EXPIRED,

    UNKNOWN_ERROR,
    RETRY_FAIL,

    COMPENSATE
}
