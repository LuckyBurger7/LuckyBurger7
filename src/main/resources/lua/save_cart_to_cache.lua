local cartKey = KEYS[1]
local menuKey = KEYS[2]
local shopKey = KEYS[3]
local totalPriceKey = KEYS[4]

-- JSON 문자열 Lua 테이블로 역직렬화
local shopId = ARGV[1]
local totalPrice = tonumber(ARGV[2])
local ttlSeconds = tonumber(ARGV[3])
local countArgv = (#ARGV - 3)

redis.log(redis.LOG_WARNING, "countArgv '" .. countArgv .. "'")

-- 모든 메뉴 순회
for i = 4, countArgv + 3 , 2 do
	local shopMenuId = tonumber(ARGV[i])
	local quantity = tonumber(ARGV[i + 1])
	local menuField = menuKey .. shopMenuId -- Hash 필드 이름 생성

	redis.call('HSET', KEYS[1], menuField, quantity)
end

redis.log(redis.LOG_WARNING, "shopKey '" .. shopKey .. "'")
redis.log(redis.LOG_WARNING, "menuKey '" .. menuKey .. "'")
redis.log(redis.LOG_WARNING, "totalPriceKey '" .. totalPriceKey .. "'")


-- 점포 기록
redis.call('HSET', cartKey, shopKey, shopId)

-- 총 금액 기록
redis.call('HSET', cartKey, totalPriceKey, totalPrice)

-- TTL 갱신
redis.call('EXPIRE', cartKey, ttlSeconds)

return 1