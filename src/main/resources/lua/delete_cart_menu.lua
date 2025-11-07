local currentQuantityStr = redis.call('HGET', KEYS[1], KEYS[2])
local currentQuantity = tonumber(currentQuantityStr) or 0
local unitPrice = tonumber(ARGV[1])

-- 카트 검증
if currentQuantity == 0 then
	return 0
end

redis.call('HDEL', KEYS[1], KEYS[2])

local changePrice = -currentQuantity * unitPrice

-- 총 금액 계산
redis.call('HINCRBY', KEYS[1], KEYS[3], changePrice)

-- TTL갱신
redis.call('EXPIRE', KEYS[1], ARGV[2])

return 1