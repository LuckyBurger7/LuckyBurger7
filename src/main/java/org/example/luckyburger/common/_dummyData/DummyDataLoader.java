package org.example.luckyburger.common._dummyData;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final UserService userService;
    private final AuthService authService;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final ShopMenuRepository shopMenuRepository;


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

        Menu burger = menuRepository.save(Menu.of("치즈버거", MenuCategory.HAMBURGER, 5500));
        Menu fries = menuRepository.save(Menu.of("감자튀김", MenuCategory.SIDE, 2500));
        Menu coke = menuRepository.save(Menu.of("콜라", MenuCategory.DRINK, 2000));

        shopMenuRepository.save(ShopMenu.of(shop1, burger, ShopMenuStatus.ON_SALE, 200));
        shopMenuRepository.save(ShopMenu.of(shop1, fries, ShopMenuStatus.ON_SALE, 150));
        shopMenuRepository.save(ShopMenu.of(shop1, coke, ShopMenuStatus.ON_SALE, 300));

        shopMenuRepository.save(ShopMenu.of(shop2, burger, ShopMenuStatus.OUT_OF_STOCK, 500));
        shopMenuRepository.save(ShopMenu.of(shop2, fries, ShopMenuStatus.ON_SALE, 250));
        shopMenuRepository.save(ShopMenu.of(shop2, coke, ShopMenuStatus.ON_SALE, 400));

    }
}
