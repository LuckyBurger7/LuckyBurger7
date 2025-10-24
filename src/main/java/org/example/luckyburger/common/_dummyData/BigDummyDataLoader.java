//package org.example.luckyburger.common._dummyData;
//
//import lombok.RequiredArgsConstructor;
//import net.datafaker.Faker;
//import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
//import org.example.luckyburger.domain.auth.entity.Account;
//import org.example.luckyburger.domain.auth.enums.AccountRole;
//import org.example.luckyburger.domain.auth.repository.AccountRepository;
//import org.example.luckyburger.domain.auth.service.AuthService;
//import org.example.luckyburger.domain.order.entity.Order;
//import org.example.luckyburger.domain.order.enums.OrderStatus;
//import org.example.luckyburger.domain.order.repository.OrderRepository;
//import org.example.luckyburger.domain.shop.entity.Shop;
//import org.example.luckyburger.domain.shop.enums.BusinessStatus;
//import org.example.luckyburger.domain.shop.repository.ShopRepository;
//import org.example.luckyburger.domain.user.entity.User;
//import org.example.luckyburger.domain.user.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//@Component
//@RequiredArgsConstructor
//public class BigDummyDataLoader implements CommandLineRunner {
//
//    private final AuthService authService;
//    private final AccountRepository accountRepository;
//    private final UserRepository userRepository;
//    private final ShopRepository shopRepository;
//    private final OrderRepository orderRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
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
//        for (int a = 1; a <= 10; a++) {
//            owners.add(Account.of(
//                    "owner" + a + "@naver.com",
//                    "password",
//                    "점주" + a,
//                    AccountRole.ROLE_OWNER
//            ));
//        }
//        accountRepository.saveAll(owners);
//
//        // 유저용 account 생성
//        List<Account> accounts = new ArrayList<>();
//        for (int b = 1; b <= 10; b++) {
//            accounts.add(Account.of(
//                    "user" + b + "@naver.com",
//                    faker.name().fullName().replaceAll("\\s+", ""),
//                    "password",
//                    AccountRole.ROLE_USER
//            ));
//        }
//        accountRepository.saveAll(accounts);
//
//        // 휴대폰 번호 생성 10000개 (중복 X)
//        Set<String> phoneSet = new HashSet<>();
//        while (phoneSet.size() < 10) {
//            phoneSet.add(faker.phoneNumber().phoneNumber());
//        }
//        List<String> phoneList = new ArrayList<>(phoneSet);
//
//        // 유저 생성
//        List<User> users = new ArrayList<>();
//        for (int c = 0; c < 10; c++) {
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
//        for (int d = 0; d < 10; d++) {
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
//        for (int e = 0; e < 10; e++) {
//            int randomShop = random.nextInt(150);
//            int randomUser = random.nextInt(10000);
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
//    }
//}
