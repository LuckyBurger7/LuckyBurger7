package org.example.luckyburger.domain.coupon;

import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.example.luckyburger.domain.coupon.repository.UserCouponRepository;
import org.example.luckyburger.domain.coupon.service.CouponUserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ActiveProfiles("test")
public class CouponIssueConcurrencyTest {
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    UserCouponRepository userCouponRepository;
    @Autowired
    CouponUserService couponUserService;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    Long couponId = 1L;
    int initialStock = 30;

    @BeforeEach
    void checkRedis() {
        String stockKey = "stock:coupon:" + couponId;
        String got = redisTemplate.opsForValue().get(stockKey);
        assertNotNull(got, "Redis stock GET이 null입니다 (커넥션/직렬화/DB index 확인).");
        assertEquals(String.valueOf(initialStock), got, "Redis stock 값이 일치하지 않습니다.");
    }

    /*
        BigDummyDataLoader 기준
     */
    /*
    @Test
    void 동시_발급_테스트_Without_동시성제어() throws Exception {
        int users = 1000;
        int dummy_owners = 151;

        ExecutorService pool = Executors.newFixedThreadPool(users);
        CountDownLatch ready = new CountDownLatch(users);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(users);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        for (int i = 1; i <= users; i++) {
            final long userId = i + dummy_owners;
            final String userEmail = "user" + i + "@naver.com"; // BigDummyDataLoader 기준
            pool.submit(() -> {
                try {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            new AuthAccount(userId, userEmail, AccountRole.ROLE_USER),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);

                    ready.countDown();         // 준비 완료 알림
                    start.await();             // 모두 준비될 때까지 대기

                    couponUserService.issueCoupon(couponId);
                    success.incrementAndGet();

                } catch (Exception e) {
                    failed.incrementAndGet();
                } finally {
                    SecurityContextHolder.clearContext();
                    done.countDown();
                }
            });
        }

        ready.await(10, TimeUnit.SECONDS);
        start.countDown();

        boolean finished = done.await(60, TimeUnit.SECONDS);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        assertTrue(finished, "작업이 시간 내 끝나지 않았습니다 (교착/데드락 의심).");

        // 결과 집계
        Coupon after = couponRepository.findById(couponId).orElseThrow();
        long issuedCount = userCouponRepository.count();
        int remaining = after.getCount();

        System.out.printf("success=%d, failed=%d, issued=%d, remaining=%d%n",
                success.get(), failed.get(), issuedCount, remaining);

        // 동시성 실패 검증 (하나라도 참이면 동시성 문제 발생)
        boolean concurrencyBroken =
                issuedCount > initialStock        // 초과 발급
                        || remaining < 0                     // 음수 재고
                        || (issuedCount < initialStock && failed.get() > 0); // 비일관 상태

        assertTrue(concurrencyBroken,
                String.format("동시성 실패 재현 안됨: issued=%d, remaining=%d, success=%d, failed=%d",
                        issuedCount, remaining, success.get(), failed.get()));
    }

    @Test
    void 동시_발급_테스트_With_동시성제어_v2() throws Exception {
        int users = 300;
        int dummy_owners = 151;

        ExecutorService pool = Executors.newFixedThreadPool(users);
        CountDownLatch ready = new CountDownLatch(users);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(users);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();

        for (int i = 1; i <= users; i++) {
            final long userId = i + dummy_owners;
            final String userEmail = "user" + i + "@naver.com"; // BigDummyDataLoader 기준
            pool.submit(() -> {
                try {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            new AuthAccount(userId, userEmail, AccountRole.ROLE_USER),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);

                    ready.countDown();         // 준비 완료 알림
                    start.await();             // 모두 준비될 때까지 대기

                    couponUserService.issueCouponWithRedis(couponId);
                    success.incrementAndGet();

                } catch (Exception e) {
                    failed.incrementAndGet();
                } finally {
                    SecurityContextHolder.clearContext();
                    done.countDown();
                }
            });
        }

        ready.await(10, TimeUnit.SECONDS);
        start.countDown();

        boolean finished = done.await(60, TimeUnit.SECONDS);
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        assertTrue(finished, "작업이 시간 내 끝나지 않았습니다 (교착/데드락 의심).");

        // 결과 집계
        int expectedIssued = Math.min(users, initialStock);

        String stockKey = "stock:coupon:" + couponId;
        int redisRemaining = Integer.parseInt(
                Optional.ofNullable(redisTemplate.opsForValue().get(stockKey)).orElse("0")
        );
        long issuedCount = userCouponRepository.count();

        System.out.printf("success=%d, failed=%d, issued(DB)=%d, redisRemaining=%d%n",
                success.get(), failed.get(), issuedCount, redisRemaining);

        // === 성공 기준 검증 ===
        // 1) 총 발급 수 == 기대치 (users vs stock 중 작은 값)
        assertEquals(expectedIssued, issuedCount, "발급 수가 기대치와 다릅니다.");

        // 2) Redis 남은 재고 == initialStock - 발급 수
        assertEquals(initialStock - expectedIssued, redisRemaining, "Redis 재고가 맞지 않습니다.");

        // 3) 성공/실패 개수 체크 (동기 발급이면 바로 이 값이 나와야 함)
        assertEquals(expectedIssued, success.get(), "성공 횟수가 기대치와 다릅니다.");
        assertEquals(users - expectedIssued, failed.get(), "실패 횟수가 기대치와 다릅니다.");

        // 4) 초과 발급/음수 재고 같은 치명적 불일치가 없어야 함
        assertTrue(issuedCount <= initialStock, "초과 발급 발생!");
        assertTrue(redisRemaining >= 0, "Redis 재고가 음수입니다!");
    }
     */
}
