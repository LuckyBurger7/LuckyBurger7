local currentShopId = redis.call('HGET', KEYS[1], KEYS[2])
local newShopId = ARGV[1]
local unitPrice = tonumber(ARGV[2])

-- 점포 검증
if currentShopId and currentShopId ~= newShopId then
	return 0
end

-- 점포 사용
if not currentShopId then
	redis.call('HSET', KEYS[1], KEYS[2], newShopId)
end

-- 카트 메뉴 생성 및 수량 증가
redis.call('HINCRBY', KEYS[1], KEYS[3], 1)

-- 로그용 코드
-- redis.log(redis.LOG_WARNING, "ARGV[2] = '" .. ARGV[2] .. "'")
-- redis.log(redis.LOG_WARNING, "LEN = " .. string.len(ARGV[2]))

-- 정수화
--local clean = ARGV[2]:gsub('"', '')
--local longValue = tonumber(clean)

-- 총합 금액 계산
redis.call('HINCRBY', KEYS[1], KEYS[4], unitPrice)

-- TTL설정
redis.call('EXPIRE', KEYS[1], ARGV[3])

return 1
