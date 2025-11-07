-- 점주 주문 조회 index 설정
ALTER TABLE orders
    ADD INDEX ix_orders_shop_date_id (shop_id, order_date DESC, id DESC),
    ALGORITHM=INPLACE, LOCK=NONE;