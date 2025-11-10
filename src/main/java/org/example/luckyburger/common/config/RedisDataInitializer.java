//package org.example.luckyburger.common.config;
//
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.Objects;
//
//@Configuration
//public class RedisDataInitializer {
//
//    @Bean
//    @Order(1)
//    public ApplicationRunner redisFlusher(RedisTemplate<String, String> redisTemplate) {
//        return args -> {
//            // Redis 연결을 얻어와 flushAll 명령을 실행합니다.
//            // flushAll: 모든 DB의 모든 Key를 삭제 (주의 필요)
//            RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
//            //connection.serverCommands().flushAll();
//
//            // 또는 현재 DB만 비우는 flushDb 사용
//            connection.flushDb();
//
//            connection.close();
//            System.out.println("✅ Redis 데이터베이스가 성공적으로 초기화(FlushDB)되었습니다.");
//        };
//    }
//}
