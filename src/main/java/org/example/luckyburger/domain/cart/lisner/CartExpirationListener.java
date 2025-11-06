package org.example.luckyburger.domain.cart.lisner;

import org.example.luckyburger.domain.cart.repository.CartCacheRepository;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class CartExpirationListener implements MessageListener {
    private final String CART_USER_TIMER_PREFIX = "cart:user:timer:";

    private final RedisMessageListenerContainer container;
    private final CartCacheRepository cartCacheRepository; // DB 저장 서비스

    // 생성자
    public CartExpirationListener(
            RedisMessageListenerContainer container,
            CartCacheRepository cartCacheRepository) {

        this.container = container;
        this.cartCacheRepository = cartCacheRepository;

        // TTL 만료 콜백 패턴: __keyevent@<DB번호>__:expired
        PatternTopic topic = new PatternTopic("__keyevent@0__:expired");

        container.addMessageListener(this, topic);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());

        // 만료된 키가 타이머 키인지 확인
        if (expiredKey.startsWith(CART_USER_TIMER_PREFIX)) {
            // 데이터 키 추출
            Long accountId = Long.parseLong(expiredKey.replace(CART_USER_TIMER_PREFIX, ""));

            // DB 저장 로직 실행
            cartCacheRepository.saveAllCache(accountId);
        }
    }
}