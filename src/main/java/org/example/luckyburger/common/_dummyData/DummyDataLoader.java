package org.example.luckyburger.common._dummyData;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.OwnerSignupRequest;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.auth.service.AdminAuthService;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.service.CartUserService;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.enums.CouponType;
import org.example.luckyburger.domain.coupon.service.CouponAdminService;
import org.example.luckyburger.domain.event.dto.request.EventCreateRequest;
import org.example.luckyburger.domain.event.service.EventAdminService;
import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
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
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {

    private final UserService userService;
    private final AuthService authService;
    private final CouponAdminService couponAdminService;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final ShopMenuRepository shopMenuRepository;
    private final AccountRepository accountRepository;
    private final ShopAdminService shopAdminService;
    private final MenuAdminService menuAdminService;
    private final OrderUserService orderUserService;
    private final CartUserService cartUserService;
    private final AdminAuthService adminAuthService;
    private final OrderOwnerService orderOwnerService;
    private final ReviewUserService reviewUserService;
    private final ReviewOwnerService reviewOwnerService;
    private final ShopOwnerService shopOwnerService;
    private final EventAdminService eventAdminService;


    @Override
    @Transactional
    public void run(String... args) {
        authService.createAccount(new AccountSignupRequest(
                "admin@naver.com",
                "password",
                "관리자"
        ), AccountRole.ROLE_ADMIN);

        ShopResponse shopResp1 = shopAdminService.createShop(new ShopRequest(
                "럭키버거 홍대점",
                "서울특별시 마포구 양화로 123",
                "홍대거리"
        ));

        ShopResponse shopResp2 = shopAdminService.createShop(new ShopRequest(
                "럭키버거 강남점",
                "서울특별시 강남구 테헤란로 456",
                "강남대로"
        ));

        adminAuthService.createOwner(new OwnerSignupRequest(
                "owner1@naver.com",
                "password",
                "점주1",
                shopResp1.shopId()
        ));

        adminAuthService.createOwner(new OwnerSignupRequest(
                "owner2@naver.com",
                "password",
                "점주2",
                shopResp2.shopId()
        ));

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

        eventAdminService.createEventResponse(new EventCreateRequest(
                "신규 오픈 10% 할인 이벤트",
                "선착순 100명에게 10% 할인 쿠폰을 드립니다!"
        ));

        // TODO: CouponPolicy 추가 로직 필요 (createCoupon, createShop)
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
                100,
                LocalDateTime.now().minusDays(1),
                CouponType.RATIO
        ));

        MenuResponse menuResp1 = menuAdminService.createMenu(new MenuCreateRequest(
                "치즈버거",
                MenuCategory.HAMBURGER,
                5500
        ));

        MenuResponse menuResp2 = menuAdminService.createMenu(new MenuCreateRequest(
                "감자튀김",
                MenuCategory.SIDE,
                2500
        ));

        MenuResponse menuResp3 = menuAdminService.createMenu(new MenuCreateRequest(
                "콜라",
                MenuCategory.DRINK,
                2000
        ));

        Shop shop1 = shopRepository.getReferenceById(shopResp1.shopId());
        Shop shop2 = shopRepository.getReferenceById(shopResp2.shopId());
        Menu burger = menuRepository.getReferenceById(menuResp1.id());
        Menu fries = menuRepository.getReferenceById(menuResp2.id());
        Menu coke = menuRepository.getReferenceById(menuResp3.id());

        // TODO: ShopMenu 추가 로직 필요 (createShop, createMenu)
        ShopMenu shopMenu11 = shopMenuRepository.save(ShopMenu.of(shop1, burger, ShopMenuStatus.ON_SALE, 0));
        ShopMenu shopMenu12 = shopMenuRepository.save(ShopMenu.of(shop1, fries, ShopMenuStatus.ON_SALE, 0));
        ShopMenu shopMenu13 = shopMenuRepository.save(ShopMenu.of(shop1, coke, ShopMenuStatus.ON_SALE, 0));

        ShopMenu shopMenu21 = shopMenuRepository.save(ShopMenu.of(shop2, burger, ShopMenuStatus.ON_SALE, 0));
        ShopMenu shopMenu22 = shopMenuRepository.save(ShopMenu.of(shop2, fries, ShopMenuStatus.ON_SALE, 0));
        ShopMenu shopMenu23 = shopMenuRepository.save(ShopMenu.of(shop2, coke, ShopMenuStatus.ON_SALE, 0));

        Account user1 = accountRepository.findByEmail("user1@naver.com").orElseThrow();
        Account user2 = accountRepository.findByEmail("user2@naver.com").orElseThrow();

        Account owner1 = accountRepository.findByEmail("owner1@naver.com").orElseThrow();
        Account owner2 = accountRepository.findByEmail("owner2@naver.com").orElseThrow();

        asAccount(owner1, () -> {
            // 매장 영업 상태 변경 (OPEN)
            shopOwnerService.updateShopStatus(shop1.getId(), new ShopUpdateRequest(BusinessStatus.OPEN));
        });
        asAccount(owner2, () -> {
            // 매장 영업 상태 변경 (OPEN)
            shopOwnerService.updateShopStatus(shop2.getId(), new ShopUpdateRequest(BusinessStatus.OPEN));
        });


        final OrderResponse[] orderResp = new OrderResponse[3];

        asAccount(user1, () -> {
            //Shop1, ShopMenu1 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu11.getId()));
            OrderPrepareResponse resp1 = orderUserService.prepareOrderResponse();
            orderResp[0] = orderUserService.createOrderResponse(new OrderCreateRequest(shop1.getId(), resp1.receiver(), resp1.phone(),
                    resp1.address(), resp1.street(), "없음", null, 0
            ));

            //Shop1, ShopMenu2 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu12.getId()));
            OrderPrepareResponse resp2 = orderUserService.prepareOrderResponse();
            orderResp[1] = orderUserService.createOrderResponse(new OrderCreateRequest(shop1.getId(), resp2.receiver(), resp2.phone(),
                    resp2.address(), resp2.street(), "없음", null, 0
            ));

            //Shop2, ShopMenu1 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu21.getId()));
            OrderPrepareResponse resp3 = orderUserService.prepareOrderResponse();
            orderUserService.createOrderResponse(new OrderCreateRequest(shop2.getId(), resp3.receiver(), resp3.phone(),
                    resp3.address(), resp3.street(), "없음", null, 0
            ));

            //Shop1, ShopMenu1 장바구니
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu11.getId()));
        });

        asAccount(user2, () -> {
            //Shop1, ShopMenu3 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu13.getId()));
            OrderPrepareResponse resp1 = orderUserService.prepareOrderResponse();
            orderResp[2] = orderUserService.createOrderResponse(new OrderCreateRequest(shop1.getId(), resp1.receiver(), resp1.phone(),
                    resp1.address(), resp1.street(), "없음", null, 0
            ));

            //Shop2, ShopMenu3 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu23.getId()));
            OrderPrepareResponse resp2 = orderUserService.prepareOrderResponse();
            orderUserService.createOrderResponse(new OrderCreateRequest(shop2.getId(), resp2.receiver(), resp2.phone(),
                    resp2.address(), resp2.street(), "없음", null, 0
            ));

            //Shop2, ShopMenu2 주문
            cartUserService.addCartMenu(new CartAddMenuRequest(shopMenu22.getId()));
            OrderPrepareResponse resp3 = orderUserService.prepareOrderResponse();
            orderUserService.createOrderResponse(new OrderCreateRequest(shop2.getId(), resp3.receiver(), resp3.phone(),
                    resp3.address(), resp3.street(), "없음", null, 0
            ));
        });

        asAccount(owner1, () -> {
            orderOwnerService.updateOrderStatus(orderResp[0].orderId(), new OrderUpdateRequest(OrderStatus.COOKING));
            orderOwnerService.updateOrderStatus(orderResp[0].orderId(), new OrderUpdateRequest(OrderStatus.ON_DELIVERY));
            orderOwnerService.updateOrderStatus(orderResp[0].orderId(), new OrderUpdateRequest(OrderStatus.COMPLETED));

            orderOwnerService.updateOrderStatus(orderResp[1].orderId(), new OrderUpdateRequest(OrderStatus.COOKING));
            orderOwnerService.updateOrderStatus(orderResp[1].orderId(), new OrderUpdateRequest(OrderStatus.ON_DELIVERY));
            orderOwnerService.updateOrderStatus(orderResp[1].orderId(), new OrderUpdateRequest(OrderStatus.COMPLETED));
        });

        final ReviewResponse[] reviewResp = new ReviewResponse[2];
        asAccount(user1, () -> {
            //Shop1, ShopMenu1 주문 리뷰 작성
            reviewResp[0] = reviewUserService.createOrderReviewResponse(orderResp[0].orderId(), new ReviewRequest(
                    "잘 먹었습니다.", 4.8
            ));

            //Shop1, ShopMenu2 주문 리뷰 작성
            reviewUserService.createOrderReviewResponse(orderResp[1].orderId(), new ReviewRequest(
                    "배달이 너무 늦었습니다.", 2
            ));
        });

        asAccount(user2, () -> {
            //Shop1, ShopMenu3 주문 리뷰 작성
            reviewResp[1] = reviewUserService.createOrderReviewResponse(orderResp[2].orderId(), new ReviewRequest(
                    "감자튀김이 좀 식었어요.", 3.5
            ));
        });

        asAccount(owner1, () -> {
            reviewOwnerService.createComment(reviewResp[0].id(), new CommentRequest(
                    "주문해주셔서 감사합니다."
            ));

            reviewOwnerService.createComment(reviewResp[1].id(), new CommentRequest(
                    "주문해주셔서 감사합니다."
            ));
        });
    }

    private void asAccount(Account account, Runnable task) {
        var principal = new AuthAccount(account.getId(), account.getEmail(), account.getRole());
        var auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority(account.getRole().name()))
        );

        var ctx = SecurityContextHolder.getContext();
        var prev = ctx.getAuthentication();
        try {
            ctx.setAuthentication(auth);
            task.run(); // 기존 서비스 호출
        } finally {
            ctx.setAuthentication(prev); // 복원
        }
    }
}
