package org.example.luckyburger;

import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.coupon.service.UserCouponEntityFinder;
import org.example.luckyburger.domain.order.dto.response.OrderCouponResponse;
import org.example.luckyburger.domain.order.entity.OrderForm;
import org.example.luckyburger.domain.order.repository.OrderFormRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class OrderInfoTest {

    @Autowired
    private CartEntityFinder cartEntityFinder;

    @Autowired
    private CartMenuEntityFinder cartMenuEntityFinder;

    @Autowired
    private OrderFormRepository orderFormRepository;

    @Autowired
    private ShopEntityFinder shopEntityFinder;

    @Autowired
    private UserCouponEntityFinder userCouponEntityFinder;

    @Autowired
    private org.example.luckyburger.domain.user.repository.UserRepository userRepository;

    @Test
    void concurrentPrepareOrderWithRealUsers() throws InterruptedException {
        int threadCount = 300;
        List<TimingResult> results = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(50);

        List<User> users = userRepository.findAll(); // DB에 실제 존재하는 유저 리스트

        for (int i = 0; i < threadCount; i++) {
            int idx = i % users.size();
            executor.submit(() -> {
                TimingResult tr = new TimingResult();
                try {
                    long start;

                    User user = users.get(idx);

                    start = System.nanoTime();
                    Cart cart = cartEntityFinder.getCartByUserId(user.getId());
                    tr.cartTime = System.nanoTime() - start;

                    start = System.nanoTime();
                    List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId());
                    tr.cartMenuTime = System.nanoTime() - start;

                    start = System.nanoTime();
                    orderFormRepository.deleteByUser(user);
                    List<OrderForm> orderFormsToSave = cartMenus.stream()
                            .map(cm -> OrderForm.of(user, cm.getShopMenu(), cm.getQuantity()))
                            .toList();
                    orderFormRepository.saveAll(orderFormsToSave);
                    tr.orderSaveTime = System.nanoTime() - start;

                    start = System.nanoTime();
                    Shop shop = shopEntityFinder.getShopById(cartMenus.get(0).getShopMenu().getShop().getId());
                    tr.shopTime = System.nanoTime() - start;

                    start = System.nanoTime();
                    var coupons = userCouponEntityFinder.getAllVerifiedUserCouponByUserId(user.getId())
                            .stream().map(OrderCouponResponse::from).toList();
                    tr.couponTime = System.nanoTime() - start;

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                results.add(tr);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, java.util.concurrent.TimeUnit.MINUTES);

        double avgCart = results.stream().mapToLong(r -> r.cartTime).average().orElse(0) / 1_000_000.0;
        double avgCartMenu = results.stream().mapToLong(r -> r.cartMenuTime).average().orElse(0) / 1_000_000.0;
        double avgOrderSave = results.stream().mapToLong(r -> r.orderSaveTime).average().orElse(0) / 1_000_000.0;
        double avgShop = results.stream().mapToLong(r -> r.shopTime).average().orElse(0) / 1_000_000.0;
        double avgCoupon = results.stream().mapToLong(r -> r.couponTime).average().orElse(0) / 1_000_000.0;

        System.out.printf("평균 수행 시간(ms) - Cart: %.2f, CartMenu: %.2f, OrderSave: %.2f, Shop: %.2f, Coupon: %.2f%n",
                avgCart, avgCartMenu, avgOrderSave, avgShop, avgCoupon);
    }

    static class TimingResult {
        long cartTime;
        long cartMenuTime;
        long orderSaveTime;
        long shopTime;
        long couponTime;
    }
}
