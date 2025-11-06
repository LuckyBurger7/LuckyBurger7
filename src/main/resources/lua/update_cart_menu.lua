local currentQuantityStr = redis.call('HGET', KEYS[1], KEYS[2])
local currentQuantity = tonumber(currentQuantityStr) or 0
local newQuantity = tonumber(ARGV[1]) or 0
local unitPrice = tonumber(ARGV[2])

redis.log(redis.LOG_WARNING, "ARGV[1] = '" .. currentQuantity .. "'")

-- 음수 검증
if newQuantity < 0 then
	return 0
end

-- 카트 검증
if currentQuantity == 0 then
	return 0
end

-- 메뉴 개수 수정
if newQuantity == 0 then
	redis.call('HDEL', KEYS[1], KEYS[2])
else
	redis.call('HSET', KEYS[1], KEYS[2], newQuantity)
end

local changePrice = (newQuantity - currentQuantity) * unitPrice

-- 총 금액 계산
redis.call('HINCRBY', KEYS[1], KEYS[3], changePrice)

-- TTL갱신
redis.call('EXPIRE', KEYS[1], ARGV[3])

return 1