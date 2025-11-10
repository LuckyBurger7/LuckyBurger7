//package org.example.luckyburger.common._dummyData;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//import java.util.Random;
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.datafaker.Faker;
//import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
//import org.example.luckyburger.domain.auth.entity.Account;
//import org.example.luckyburger.domain.auth.enums.AccountRole;
//import org.example.luckyburger.domain.auth.repository.AccountRepository;
//import org.example.luckyburger.domain.auth.service.AuthService;
//import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
//import org.example.luckyburger.domain.coupon.enums.CouponType;
//import org.example.luckyburger.domain.coupon.service.CouponAdminService;
//import org.example.luckyburger.domain.order.entity.Order;
//import org.example.luckyburger.domain.order.enums.OrderStatus;
//import org.example.luckyburger.domain.order.repository.OrderRepository;
//import org.example.luckyburger.domain.shop.entity.Shop;
//import org.example.luckyburger.domain.shop.enums.BusinessStatus;
//import org.example.luckyburger.domain.shop.repository.ShopRepository;
//import org.example.luckyburger.domain.user.entity.User;
//import org.example.luckyburger.domain.user.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//@org.springframework.core.annotation.Order(2)
//public class BigDummyDataLoader implements CommandLineRunner {
//
//    private final AuthService authService;
//    private final AccountRepository accountRepository;
//    private final UserRepository userRepository;
//    private final ShopRepository shopRepository;
//    private final OrderRepository orderRepository;
//    private final CouponAdminService couponAdminService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//
//        //=== 멱등 마커: 관리자 계정이 이미 있으면 전체 시딩 스킵 ===
//        if (accountRepository.findByEmail("admin@naver.com").isPresent()) {
//            log.info("[DummyDataLoader] admin@naver.com already exists. Skip seeding.");
//            return;
//        }
//
//        Faker faker = new Faker(new Locale("ko"));
//        Random random = new Random();
//
//        // 관리자 account 생성
//        authService.createAccount(new AccountSignupRequest(
//                "admin@naver.com",
//                "password",
//                "관리자"
//        ), AccountRole.ROLE_ADMIN);
//
//        // 점주 account 생성
//        List<Account> owners = new ArrayList<>();
//        for (int a = 1; a <= 150; a++) {
//            owners.add(Account.of(
//                    "owner" + a + "@naver.com",
//                    "점주" + a,
//                    passwordEncoder.encode("password"),
//                    AccountRole.ROLE_OWNER
//            ));
//        }
//        accountRepository.saveAll(owners);
//
//        // 유저용 account 생성
//        List<Account> accounts = new ArrayList<>();

//        for (int b = 1; b <= 1000; b++) {
//            accounts.add(Account.of(
//                    "user" + b + "@naver.com",
//                    faker.name().fullName().replaceAll("\\s+", ""),
//                    passwordEncoder.encode("password"),
//                    AccountRole.ROLE_USER
//            ));
//        }
//        accountRepository.saveAll(accounts);
//
//        // 휴대폰 번호 생성 10000개 (중복 X)
//        Set<String> phoneSet = new HashSet<>();
//        while (phoneSet.size() < 1000) {
//            phoneSet.add(faker.phoneNumber().phoneNumber());
//        }
//        List<String> phoneList = new ArrayList<>(phoneSet);
//
//        // 유저 생성
//        List<User> users = new ArrayList<>();
//        for (int c = 0; c < 1000; c++) {
//            users.add(User.of(accounts.get(c),
//                    phoneList.get(c),
//                    faker.address().city(),
//                    faker.address().streetAddress()
//            ));
//        }
//        userRepository.saveAll(users);
//
//        // 점포 150개 생성
//        List<Shop> shops = new ArrayList<>();
//        for (int d = 0; d < 150; d++) {
//            String city = faker.address().cityName();
//            shops.add(Shop.of(
//                    "럭키버거 " + city + "점",
//                    BusinessStatus.CLOSED,
//                    city,
//                    faker.address().streetAddress()
//            ));
//        }
//        shopRepository.saveAll(shops);
//
//        // 주문 10만개 생성
//        List<Order> orders = new ArrayList<>();
//        for (int e = 0; e < 100; e++) {
//            int randomShop = random.nextInt(150);
//            int randomUser = random.nextInt(1000);
//            int randomPrice = (20 + random.nextInt(80)) * 500;
//
//            orders.add(Order.of(
//                    shops.get(randomShop),
//                    users.get(randomUser),
//                    users.get(randomUser).getAccount().getName(),
//                    users.get(randomUser).getPhone(),
//                    users.get(randomUser).getAddress(),
//                    users.get(randomUser).getStreet(),
//                    null,
//                    null,
//                    0,
//                    randomPrice,
//                    randomPrice,
//                    LocalDateTime.now(),
//                    OrderStatus.COMPLETED
//            ));
//        }
//        orderRepository.saveAll(orders);
//
//        couponAdminService.createCouponWithRedis(new CouponRequest(
//                "10프로 할인 쿠폰",
//                0.1,
//                30,
//                LocalDateTime.now().plusDays(1),
//                CouponType.RATIO,
//                LocalDateTime.now()
//        ));
//    }
//}
