package org.example.luckyburger.common._dummyData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.auth.repository.OwnerRepository;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BigDummyDataLoader implements CommandLineRunner {

    private final AuthService authService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final OrderRepository orderRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        //=== 멱등 마커: 관리자 계정이 이미 있으면 전체 시딩 스킵 ===
        if (accountRepository.findByEmail("admin@naver.com").isPresent()) {
            log.info("[DummyDataLoader] admin@naver.com already exists. Skip seeding.");
            return;
        }

        Faker faker = new Faker(new Locale("ko"));
        Random random = new Random();

        // 관리자 account 생성
        authService.createAccount(new AccountSignupRequest(
                "admin@naver.com",
                "password",
                "관리자"
        ), AccountRole.ROLE_ADMIN);

        // 점주 account 생성
        List<Account> owners = new ArrayList<>();
        final String RAW_PWs = "password";
        for (int a = 1; a <= 150; a++) {
            owners.add(Account.of(
                    "owner" + a + "@naver.com",
                    "점주" + a,
                    passwordEncoder.encode(RAW_PWs),
                    AccountRole.ROLE_OWNER
            ));
        }
        accountRepository.saveAll(owners);

        // 유저용 account 생성
        List<Account> accounts = new ArrayList<>();
        final String RAW_PW = "password";
        for (int b = 1; b <= 150; b++) {
            accounts.add(Account.of(
                    "user" + b + "@naver.com",
                    faker.name().fullName().replaceAll("\\s+", ""),
                    passwordEncoder.encode(RAW_PW),
                    AccountRole.ROLE_USER
            ));
        }
        accountRepository.saveAll(accounts);

        // 휴대폰 번호 생성 10000개 (중복 X)
        Set<String> phoneSet = new HashSet<>();
        while (phoneSet.size() < 150) {
            phoneSet.add(faker.phoneNumber().phoneNumber());
        }
        List<String> phoneList = new ArrayList<>(phoneSet);

        // 유저 생성
        List<User> users = new ArrayList<>();
        for (int c = 0; c < 150; c++) {
            users.add(User.of(accounts.get(c),
                    phoneList.get(c),
                    faker.address().city(),
                    faker.address().streetAddress()
            ));
        }
        userRepository.saveAll(users);

        // 점포 150개 생성
        List<Shop> shops = new ArrayList<>();
        for (int d = 0; d < 150; d++) {
            String city = faker.address().cityName();
            shops.add(Shop.of(
                    "럭키버거 " + city + "점",
                    BusinessStatus.CLOSED,
                    city,
                    faker.address().streetAddress()
            ));
        }
        shopRepository.saveAll(shops);

        // 주문 10만개 생성
        List<Order> orders = new ArrayList<>();
        for (int e = 0; e < 100000; e++) {
            int randomShop = random.nextInt(150);
            int randomUser = random.nextInt(150);
            int randomPrice = (20 + random.nextInt(80)) * 500;

            orders.add(Order.of(
                    shops.get(randomShop),
                    users.get(randomUser),
                    users.get(randomUser).getAccount().getName(),
                    users.get(randomUser).getPhone(),
                    users.get(randomUser).getAddress(),
                    users.get(randomUser).getStreet(),
                    null,
                    null,
                    0,
                    randomPrice,
                    randomPrice,
                    LocalDateTime.now(),
                    OrderStatus.COMPLETED
            ));
        }
        orderRepository.saveAll(orders);

        List<Owner> ownerEntities = new ArrayList<>(owners.size());
        int pairCount = Math.min(owners.size(), shops.size());

        for (int i = 0; i < owners.size(); i++) {
            Account ownerAccount = owners.get(i);
            Shop shop = shops.get(i);

            Owner owner = Owner.of(ownerAccount, shop);
            ownerEntities.add(owner);

            ownerRepository.saveAll(ownerEntities);
        }
    }
}
