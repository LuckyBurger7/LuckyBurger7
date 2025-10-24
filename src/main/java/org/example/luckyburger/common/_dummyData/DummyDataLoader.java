package org.example.luckyburger.common._dummyData;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.enums.CouponType;
import org.example.luckyburger.domain.coupon.service.CouponAdminService;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final UserService userService;
    private final AuthService authService;
    private final CouponAdminService couponAdminService;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final ShopMenuRepository shopMenuRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final ReviewRepository reviewRepository;


    @Override
    @Transactional
    public void run(String... args) {
        authService.createAccount(new AccountSignupRequest(
                "admin@naver.com",
                "password",
                "관리자"
        ), AccountRole.ROLE_ADMIN);

        authService.createAccount(new AccountSignupRequest(
                "owner@naver.com",
                "password",
                "점주"
        ), AccountRole.ROLE_OWNER);

        userService.createUser(new UserSignupRequest(
                "user1@naver.com",
                "password",
                "김기수",
                "010-3333-5555",
                "주소",
                "상세 주소"
        ));
        userService.createUser(new UserSignupRequest(
                "user2@naver.com",
                "password",
                "홍길동",
                "010-7777-8888",
                "주소",
                "상세 주소"
        ));

        Shop shop1 = shopRepository.save(
                Shop.builder()
                        .name("럭키버거 홍대점")
                        .status(BusinessStatus.OPEN)
                        .address("서울특별시 마포구 양화로 123")
                        .street("홍대거리")
                        .build()
        );

        Shop shop2 = shopRepository.save(
                Shop.builder()
                        .name("럭키버거 강남점")
                        .status(BusinessStatus.CLOSED)
                        .address("서울특별시 강남구 테헤란로 456")
                        .street("강남대로")
                        .build()
        );

        couponAdminService.createCoupon(new CouponRequest(
                "5000원 할인 쿠폰",
                5000.0,
                5,
                LocalDateTime.now().plusDays(1),
                CouponType.FIXED
        ));
        couponAdminService.createCoupon(new CouponRequest(
                "10프로 할인 쿠폰",
                0.1,
                5,
                LocalDateTime.now().minusDays(1),
                CouponType.RATIO
        ));

        Menu burger = menuRepository.save(Menu.of("치즈버거", MenuCategory.HAMBURGER, 5500));
        Menu fries = menuRepository.save(Menu.of("감자튀김", MenuCategory.SIDE, 2500));
        Menu coke = menuRepository.save(Menu.of("콜라", MenuCategory.DRINK, 2000));

        shopMenuRepository.save(ShopMenu.of(shop1, burger, ShopMenuStatus.ON_SALE, 200));
        shopMenuRepository.save(ShopMenu.of(shop1, fries, ShopMenuStatus.ON_SALE, 150));
        shopMenuRepository.save(ShopMenu.of(shop1, coke, ShopMenuStatus.ON_SALE, 300));

        shopMenuRepository.save(ShopMenu.of(shop2, burger, ShopMenuStatus.OUT_OF_STOCK, 500));
        shopMenuRepository.save(ShopMenu.of(shop2, fries, ShopMenuStatus.ON_SALE, 250));
        shopMenuRepository.save(ShopMenu.of(shop2, coke, ShopMenuStatus.ON_SALE, 400));

        Account account1 = accountRepository.findByEmail("user1@naver.com").orElseThrow();
        Account account2 = accountRepository.findByEmail("user2@naver.com").orElseThrow();

        User user1 = userRepository.findByAccount(account1).orElseThrow();
        User user2 = userRepository.findByAccount(account2).orElseThrow();

        Order order1 = Order.builder()
                .shop(shop1)
                .user(user1)
                .receiver("김기수")
                .phone("010-3333-5555")
                .address("서울시 마포구 양화로 123")
                .street("홍대거리 1길 5")
                .request("치즈 많이 넣어주세요.")
                .point(100)
                .totalPrice(10000)
                .orderDate(LocalDateTime.now().minusHours(3))
                .status(OrderStatus.WAITING)
                .build();

        Order order2 = Order.builder()
                .shop(shop1)
                .user(user2)
                .receiver("홍길동")
                .phone("010-7777-8888")
                .address("서울시 강남구 테헤란로 456")
                .street("강남대로 2길 10")
                .request("케첩 많이 주세요.")
                .point(200)
                .totalPrice(15000)
                .orderDate(LocalDateTime.now().minusHours(1))
                .status(OrderStatus.COOKING)
                .build();

        Order order3 = Order.builder()
                .shop(shop2)
                .user(user1)
                .receiver("김기수")
                .phone("010-3333-5555")
                .address("서울시 마포구 양화로 123")
                .street("홍대거리 3길 9")
                .request(null)
                .point(0)
                .totalPrice(20000)
                .orderDate(LocalDateTime.now().minusDays(1))
                .status(OrderStatus.COMPLETED)
                .build();

        Order order4 = Order.builder()
                .shop(shop2)
                .user(user1)
                .receiver("김기수")
                .phone("010-3333-5555")
                .address("서울시 마포구 양화로 123")
                .street("홍대거리 3길 9")
                .request(null)
                .point(0)
                .totalPrice(20000)
                .orderDate(LocalDateTime.now().plusDays(1))
                .status(OrderStatus.COMPLETED)
                .build();

        Order order5 = Order.builder()
                .shop(shop2)
                .user(user1)
                .receiver("김기수")
                .phone("010-3333-5555")
                .address("서울시 마포구 양화로 123")
                .street("홍대거리 3길 9")
                .request(null)
                .point(0)
                .totalPrice(15000)
                .orderDate(LocalDateTime.of(2025, 9, 5, 12, 15))
                .status(OrderStatus.COMPLETED)
                .build();

        Order order6 = Order.builder()
                .shop(shop1)
                .user(user1)
                .receiver("김기수")
                .phone("010-3333-5555")
                .address("서울시 마포구 양화로 123")
                .street("홍대거리 3길 9")
                .request(null)
                .point(0)
                .totalPrice(115500)
                .orderDate(LocalDateTime.of(2025, 9, 22, 12, 15))
                .status(OrderStatus.COMPLETED)
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
        orderRepository.save(order5);
        orderRepository.save(order6);

        Review review1 = Review.builder()
                .user(user1)
                .shop(shop1)
                .order(order1)
                .content("치즈버거가 정말 맛있어요! 또 시킬게요 🍔")
                .rating(4.8)
                .build();

        Review review2 = Review.builder()
                .user(user2)
                .shop(shop1)
                .order(order2)
                .content("감자튀김이 좀 식었어요.")
                .rating(3.5)
                .build();

        Review review3 = Review.builder()
                .user(user1)
                .shop(shop2)
                .order(order3)
                .content("강남점은 닫혀 있어서 배달이 늦었어요 😢")
                .rating(2.0)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
    }
}
