package org.example.luckyburger.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.filter.JwtAuthenticationFilter;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderCreateResponse;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.service.OrderUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("local")
public class OrderUserControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderUserService orderUserService;
    @MockitoBean
    private JwtUtil jwtUtil;

    private TestingAuthenticationToken authPrincipal(long accountId) {
        AuthAccount principal = mock(AuthAccount.class);
        when(principal.accountId()).thenReturn(accountId);
        return new TestingAuthenticationToken(principal, null);
    }

    @Test
    void 주문_생성_성공() throws Exception {
        var auth = authPrincipal(1L);
        var request = new OrderCreateRequest(
                1L,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000
        );

        var response = sampleCreateResponse();

        given(orderUserService.createOrder(nullable(AuthAccount.class),
                any(OrderCreateRequest.class))).willReturn(response);

        mockMvc.perform(post("/api/v1/user/orders")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.orderId").value(response.orderId()))
                .andExpect(jsonPath("$.data.amount.pay").value(20000))
                .andExpect(jsonPath("$.data.items[0].name").value("치즈버거"));
    }

    @Test
    void 주문_생성_실패_유효성검사_전화번호() throws Exception {
        var auth = authPrincipal(1L);

        var invalidRequest = new OrderCreateRequest(
                7L,
                "홍길동",
                "abc-defg", // 잘못된 포맷
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000
        );

        mockMvc.perform(post("/api/v1/user/orders")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문_단일조회_성공() throws Exception {
        var auth = authPrincipal(1L);
        var response = sampleOrderResponse(9001L);

        given(orderUserService.getOrder(nullable(AuthAccount.class), eq(9001L)))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/user/orders/{orderId}", 9001L)
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(9001L))
                .andExpect(jsonPath("$.data.items.length()").value(2))
                .andExpect(jsonPath("$.data.amount.subtotal").value(23000));
    }

    @Test
    void 주문_전체조회_페이징_성공() throws Exception {
        var auth = authPrincipal(1L);
        var pageReq = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "orderDate"));

        var content = List.of(
                sampleOrderResponse(9002L),
                sampleOrderResponse(9001L)
        );
        Page<OrderResponse> page = new PageImpl<>(content, pageReq, 5);

        given(orderUserService.getAllOrder(nullable(AuthAccount.class), any(Pageable.class)))
                .willReturn(page);

        mockMvc.perform(get("/api/v1/user/orders")
                        .with(authentication(auth))
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].orderId").value(9002L))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.size").value(2));
    }

    private OrderCreateResponse sampleCreateResponse() {
        var amount = new OrderCreateResponse.Amount(23000, 3000, 20000);
        var items = List.of(
                new OrderMenuResponse(101L, "치즈버거", 7000, 2),
                new OrderMenuResponse(202L, "감자튀김", 4000, 1)
        );
        return new OrderCreateResponse(
                9001L, 1L,
                "홍길동", "010-1234-5678",
                "서울 강남구 oo", "oo아파트 101동 1001호",
                "양파 빼주세요",
                null, 5000, 500,
                amount, items,
                LocalDateTime.of(2025, 10, 17, 12, 30),
                OrderStatus.COOKING
        );
    }

    private OrderResponse sampleOrderResponse(long id) {
        var items = List.of(
                new OrderMenuResponse(101L, "치즈버거", 7000, 2),
                new OrderMenuResponse(202L, "감자튀김", 4000, 1)
        );
        var amount = new OrderResponse.Amount(23000, 20000);
        return new OrderResponse(
                id, 1L,
                "홍길동", "010-1234-5678",
                "서울 강남구 oo", "oo아파트 101동 1001호",
                "양파 빼주세요",
                null, 5000,
                amount, items,
                LocalDateTime.of(2025, 10, 17, 12, 30),
                OrderStatus.COOKING
        );
    }
}
