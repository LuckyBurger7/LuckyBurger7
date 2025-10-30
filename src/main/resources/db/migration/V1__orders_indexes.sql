SET @ddl := IF (
  EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME   = 'orders'
      AND INDEX_NAME   = 'ix_orders_acc_date_id'
  ),
  'DO 0',
  CONCAT(
    'ALTER TABLE `', DATABASE(), '`.`orders` ',
    'ALGORITHM=INPLACE, ',
    'LOCK=NONE, ',
    'ADD INDEX ix_orders_acc_date_id (account_id, order_date DESC, id DESC)'
  )
);

PREPARE stmt FROM @ddl;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;