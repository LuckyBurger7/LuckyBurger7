package org.example.luckyburger.common._dummyData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.OwnerSignupRequest;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.auth.service.AuthAdminService;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.service.CartUserService;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.enums.CouponType;
import org.example.luckyburger.domain.coupon.repository.CouponRepository;
import org.example.luckyburger.domain.coupon.service.CouponAdminService;
import org.example.luckyburger.domain.event.dto.request.EventCreateRequest;
import org.example.luckyburger.domain.event.repository.EventRepository;
import org.example.luckyburger.domain.event.service.EventAdminService;
import org.example.luckyburger.domain.menu.dto.request.MenuRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.example.luckyburger.domain.menu.service.MenuAdminService;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.request.OrderUpdateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderPrepareResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.service.OrderOwnerService;
import org.example.luckyburger.domain.order.service.OrderUserService;
import org.example.luckyburger.domain.review.dto.request.CommentRequest;
import org.example.luckyburger.domain.review.dto.request.ReviewRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.service.ReviewOwnerService;
import org.example.luckyburger.domain.review.service.ReviewUserService;
import org.example.luckyburger.domain.shop.dto.request.ShopMenuRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.example.luckyburger.domain.shop.service.ShopAdminService;
import org.example.luckyburger.domain.shop.service.ShopOwnerService;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CouponAdminService couponAdminService;
    private final ShopMenuRepository shopMenuRepository;
    private final AccountRepository accountRepository;
    private final ShopAdminService shopAdminService;
    private final MenuAdminService menuAdminService;
    private final OrderUserService orderUserService;
    private final CartUserService cartUserService;
    private final AuthAdminService authAdminService;
    private final OrderOwnerService orderOwnerService;
    private final ReviewUserService reviewUserService;
    private final ReviewOwnerService reviewOwnerService;
    private final ShopOwnerService shopOwnerService;
    private final EventAdminService eventAdminService;

    // 존재 확인용 단순 레포들
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // === 멱등 마커: 관리자 계정이 이미 있으면 전체 시딩 스킵 ===
        if (accountRepository.findByEmail("admin@naver.com").isPresent()) {
            log.info("[DummyDataLoader] admin@naver.com already exists. Skip seeding.");
            return;
        }

        // 1) 계정/유저/점주 ensure
        ensureAccount("admin@naver.com", "password", "관리자", AccountRole.ROLE_ADMIN);

        ShopResponse shopResp1 = ensureShop("럭키버거 홍대점", "서울특별시 마포구 양화로 123", "홍대거리");
        //ShopResponse shopResp2 = ensureShop("럭키버거 강남점", "서울특별시 강남구 테헤란로 456", "강남대로");

        ensureOwner("owner1@naver.com", "password", "점주1", shopResp1.shopId());
        //ensureOwner("owner2@naver.com", "password", "점주2", shopResp2.shopId());

        ensureUsers(100);

        // 2) 이벤트/쿠폰 ensure
        ensureEvent("신규 오픈 10% 할인 이벤트", "선착순 100명에게 10% 할인 쿠폰을 드립니다!");

        ensureCoupon("5000원 할인 쿠폰", 5000.0, 5,
                LocalDateTime.now().plusDays(1), CouponType.FIXED, LocalDateTime.now().plusSeconds(1));
        ensureCoupon("10프로 할인 쿠폰", 0.1, 100,
                LocalDateTime.now().minusDays(1), CouponType.RATIO, LocalDateTime.now().minusDays(1));

        // 3) 메뉴 ensure
        MenuResponse menuResp1 = ensureMenu("치즈버거", MenuCategory.HAMBURGER, 5500);
        MenuResponse menuResp2 = ensureMenu("감자튀김", MenuCategory.SIDE, 2500);
        MenuResponse menuResp3 = ensureMenu("콜라", MenuCategory.DRINK, 2000);
        MenuResponse menuResp4 = ensureMenu("불고기버거", MenuCategory.HAMBURGER, 5500);
        MenuResponse menuResp5 = ensureMenu("치킨너겟", MenuCategory.SIDE, 2500);
        MenuResponse menuResp6 = ensureMenu("사이다", MenuCategory.DRINK, 2000);
        // ShopMenu 연결(이미 존재한다고 가정: createShop/createMenu 시 ShopMenu 생성 로직이 있다면 find만)
        ShopMenu shopMenu11 = shopMenuRepository.findWithShopByShopIdAndMenuId(shopResp1.shopId(), menuResp1.menuId())
                .orElseThrow();
        ShopMenu shopMenu12 = shopMenuRepository.findWithShopByShopIdAndMenuId(shopResp1.shopId(), menuResp2.menuId())
                .orElseThrow();
        ShopMenu shopMenu13 = shopMenuRepository.findWithShopByShopIdAndMenuId(shopResp1.shopId(), menuResp3.menuId())
                .orElseThrow();
        /*ShopMenu shopMenu21 = shopMenuRepository.findWithShopByShopIdAndMenuId(shopResp2.shopId(), menuResp1.menuId())
                .orElseThrow();
        ShopMenu shopMenu22 = shopMenuRepository.findWithShopByShopIdAndMenuId(shopResp2.shopId(), menuResp2.menuId())
                .orElseThrow();
        ShopMenu shopMenu23 = shopMenuRepository.findWithShopByShopIdAndMenuId(shopResp2.shopId(), menuResp3.menuId())
                .orElseThrow();*/

        Account user1 = accountRepository.findByEmail("user1@naver.com").orElseThrow();
        Account user2 = accountRepository.findByEmail("user2@naver.com").orElseThrow();
        Account owner1 = accountRepository.findByEmail("owner1@naver.com").orElseThrow();
        //Account owner2 = accountRepository.findByEmail("owner2@naver.com").orElseThrow();

        // 4) 점주로서 매장/메뉴 상태 변경
        asAccount(owner1, () -> {
            shopOwnerService.updateShopStatus(shopResp1.shopId(), new ShopUpdateRequest(BusinessStatus.OPEN));
            shopOwnerService.updateMenuStatus(shopResp1.shopId(), menuResp1.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
            shopOwnerService.updateMenuStatus(shopResp1.shopId(), menuResp2.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
            shopOwnerService.updateMenuStatus(shopResp1.shopId(), menuResp3.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
            shopOwnerService.updateMenuStatus(shopResp1.shopId(), menuResp4.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
            shopOwnerService.updateMenuStatus(shopResp1.shopId(), menuResp5.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
        });// 6번 메뉴만 비활성화
        /*asAccount(owner2, () -> {
            shopOwnerService.updateShopStatus(shopResp2.shopId(), new ShopUpdateRequest(BusinessStatus.OPEN));
            shopOwnerService.updateMenuStatus(shopResp2.shopId(), menuResp1.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
            shopOwnerService.updateMenuStatus(shopResp2.shopId(), menuResp2.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
            shopOwnerService.updateMenuStatus(shopResp2.shopId(), menuResp3.menuId(),
                    new ShopMenuRequest(ShopMenuStatus.ON_SALE));
        });*/

        // 5) 유저 주문/장바구니/리뷰
        final OrderResponse[] orderResp = new OrderResponse[3];

        asAccount(user1, () -> {
            // Shop1, ShopMenu1 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu11.getId()));
            OrderPrepareResponse resp1 = orderUserService.prepareOrderResponse();
            orderResp[0] = orderUserService.createOrderResponse(
                    new OrderCreateRequest(shopResp1.shopId(), resp1.receiver(), resp1.phone(),
                            resp1.address(), resp1.street(), "없음", null, 0));

            // Shop1, ShopMenu2 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu12.getId()));
            OrderPrepareResponse resp2 = orderUserService.prepareOrderResponse();
            orderResp[1] = orderUserService.createOrderResponse(
                    new OrderCreateRequest(shopResp1.shopId(), resp2.receiver(), resp2.phone(),
                            resp2.address(), resp2.street(), "없음", null, 0));

            // Shop2, ShopMenu1 주문
            /*cartUserService.addCartMenuV1(new CartAddMenuRequest(shopMenu21.getId()));
            OrderPrepareResponse resp3 = orderUserService.prepareOrderResponse();
            orderUserService.createOrderResponse(
                    new OrderCreateRequest(shopResp2.shopId(), resp3.receiver(), resp3.phone(),
                            resp3.address(), resp3.street(), "없음", null, 0));*/

            // Shop1, ShopMenu1 장바구니
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu11.getId()));
        });

        asAccount(user2, () -> {
            // Shop1, ShopMenu3 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu13.getId()));
            OrderPrepareResponse resp1 = orderUserService.prepareOrderResponse();
            orderResp[2] = orderUserService.createOrderResponse(
                    new OrderCreateRequest(shopResp1.shopId(), resp1.receiver(), resp1.phone(),
                            resp1.address(), resp1.street(), "없음", null, 0));

            // Shop2, ShopMenu3 주문
            /*cartUserService.addCartMenuV1(new CartAddMenuRequest(shopMenu23.getId()));
            OrderPrepareResponse resp2 = orderUserService.prepareOrderResponse();
            orderUserService.createOrderResponse(
                    new OrderCreateRequest(shopResp2.shopId(), resp2.receiver(), resp2.phone(),
                            resp2.address(), resp2.street(), "없음", null, 0));*/

            // Shop2, ShopMenu2 주문
            /*cartUserService.addCartMenuV1(new CartAddMenuRequest(shopMenu22.getId()));
            OrderPrepareResponse resp3 = orderUserService.prepareOrderResponse();
            orderUserService.createOrderResponse(
                    new OrderCreateRequest(shopResp2.shopId(), resp3.receiver(), resp3.phone(),
                            resp3.address(), resp3.street(), "없음", null, 0));*/
        });

        // 6) 점주: 주문 상태 변경
        asAccount(owner1, () -> {
            orderOwnerService.updateOrderStatus(orderResp[0].orderId(), new OrderUpdateRequest(OrderStatus.COOKING));
            orderOwnerService.updateOrderStatus(orderResp[0].orderId(),
                    new OrderUpdateRequest(OrderStatus.ON_DELIVERY));
            orderOwnerService.updateOrderStatus(orderResp[0].orderId(), new OrderUpdateRequest(OrderStatus.COMPLETED));

            orderOwnerService.updateOrderStatus(orderResp[1].orderId(), new OrderUpdateRequest(OrderStatus.COOKING));
            orderOwnerService.updateOrderStatus(orderResp[1].orderId(),
                    new OrderUpdateRequest(OrderStatus.ON_DELIVERY));
            orderOwnerService.updateOrderStatus(orderResp[1].orderId(), new OrderUpdateRequest(OrderStatus.COMPLETED));
        });

        // 7) 리뷰 & 점주 코멘트
        final ReviewResponse[] reviewResp = new ReviewResponse[2];
        asAccount(user1, () -> {
            reviewResp[0] = reviewUserService.createOrderReviewResponse(orderResp[0].orderId(),
                    new ReviewRequest("잘 먹었습니다.", 4.8));
            reviewUserService.createOrderReviewResponse(orderResp[1].orderId(),
                    new ReviewRequest("배달이 너무 늦었습니다.", 2));
        });

        asAccount(user2, () -> {
            reviewResp[1] = reviewUserService.createOrderReviewResponse(orderResp[2].orderId(),
                    new ReviewRequest("감자튀김이 좀 식었어요.", 3.5));
        });

        asAccount(owner1, () -> {
            reviewOwnerService.createComment(shopResp1.shopId(), reviewResp[0].reviewId(),
                    new CommentRequest("주문해주셔서 감사합니다."));
            reviewOwnerService.createComment(shopResp1.shopId(), reviewResp[1].reviewId(),
                    new CommentRequest("주문해주셔서 감사합니다."));
        });

        log.info("[DummyDataLoader] Seeding completed.");
    }

    // =========================
    // 멱등 보조 메서드 (ensure)
    // =========================

    private Account ensureAccount(String email, String password, String name, AccountRole role) {
        return accountRepository.findByEmail(email).orElseGet(() -> {
            authService.createAccount(new AccountSignupRequest(email, password, name), role);
            return accountRepository.findByEmail(email).orElseThrow();
        });
    }

    private void ensureOwner(String email, String password, String name, Long shopId) {
        accountRepository.findByEmail(email).orElseGet(() -> {
            authAdminService.createOwner(new OwnerSignupRequest(email, password, name, shopId));
            return accountRepository.findByEmail(email).orElseThrow();
        });
    }

    private void ensureUser(String email, String password, String name, String phone, String addr, String street) {
        accountRepository.findByEmail(email).orElseGet(() -> {
            userService.createUser(new UserSignupRequest(email, password, name, phone, addr, street));
            return accountRepository.findByEmail(email).orElseThrow();
        });
    }

    private void ensureUsers(int count) {
        Faker faker = new Faker(new Locale("ko"));

        // 휴대폰 번호 생성 10000개 (중복 X)
        Set<String> phoneSet = new HashSet<>();
        while (phoneSet.size() < count) {
            phoneSet.add(faker.phoneNumber().phoneNumber());
        }
        List<String> phoneList = new ArrayList<>(phoneSet);

        for (int i = 0; i < count; i++) {
            ensureUser("user" + i + "@naver.com",
                    "password",
                    faker.name().fullName().replaceAll("\\s+", ""),
                    phoneList.get(i),
                    faker.address().city(),
                    faker.address().streetAddress()
            );
        }
    }

    private ShopResponse ensureShop(String name, String address, String street) {
        Optional<Shop> found = shopRepository.findByNameAndStreet(name, street);
        if (found.isPresent()) {
            return ShopResponse.from(found.get());
        }
        return shopAdminService.createShop(new ShopRequest(name, address, street));
    }

    private MenuResponse ensureMenu(String name, MenuCategory category, long price) {
        Optional<Menu> found = menuRepository.findByName(name);
        if (found.isPresent()) {
            return MenuResponse.from(found.get());
        }
        return menuAdminService.createMenu(new MenuRequest(name, category, price));
    }

    private void ensureCoupon(String name, double discount, int count,
                              LocalDateTime exp, CouponType type, LocalDateTime open) {
        couponRepository.findByName(name).orElseGet(() -> {
            couponAdminService.createCouponWithRedis(new CouponRequest(name, discount, count, exp, type, open));
            return couponRepository.findByName(name).orElseThrow();
        });
    }

    private void ensureEvent(String title, String content) {
        eventRepository.findByTitle(title).orElseGet(() -> {
            eventAdminService.createEventResponse(new EventCreateRequest(title, content));
            return eventRepository.findByTitle(title).orElseThrow();
        });
    }

    private void asAccount(Account account, Runnable task) {
        var principal = new AuthAccount(account.getId(), account.getEmail(), account.getRole());
        var auth = new UsernamePasswordAuthenticationToken(
                principal, null, List.of(new SimpleGrantedAuthority(account.getRole().name()))
        );
        var ctx = SecurityContextHolder.getContext();
        var prev = ctx.getAuthentication();
        try {
            ctx.setAuthentication(auth);
            task.run();
        } finally {
            ctx.setAuthentication(prev);
        }
    }
}
