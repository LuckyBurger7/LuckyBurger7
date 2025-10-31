-- 사용자 주문 조회 인덱싱
ALTER TABLE orders
    ADD INDEX ix_orders_acc_date_id (account_id, order_date DESC, id DESC),
    ALGORITHM=INPLACE, LOCK=NONE;