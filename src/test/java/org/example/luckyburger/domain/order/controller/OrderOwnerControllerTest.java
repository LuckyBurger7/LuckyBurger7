package org.example.luckyburger.domain.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.filter.JwtAuthenticationFilter;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.order.dto.request.OrderUpdateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.OrderStatusInvalidUpdateException;
import org.example.luckyburger.domain.order.service.OrderOwnerService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderOwnerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("local")
public class OrderOwnerControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderOwnerService orderOwnerService;
    @MockitoBean
    private JwtUtil jwtUtil;

    private TestingAuthenticationToken authPrincipal(long accountId) {
        AuthAccount principal = mock(AuthAccount.class);
        when(principal.getAccountId()).thenReturn(accountId);
        return new TestingAuthenticationToken(principal, null);
    }

    @Test
    void 주문_단일조회_성공() throws Exception {
        var auth = authPrincipal(1L);
        var response = sampleOrderResponse(9001L);

        given(orderOwnerService.getOrderResponse(eq(9001L)))
                .willReturn(response);

        mockMvc.perform(get("/api/v1/owner/orders/{orderId}", 9001L)
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

        given(orderOwnerService.getAllOrderResponse(any(Pageable.class)))
                .willReturn(page);

        mockMvc.perform(get("/api/v1/owner/orders")
                        .with(authentication(auth))
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].orderId").value(9002L))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.size").value(2));
    }

    @Test
    void 주문_상태변경_성공() throws Exception {
        var auth = authPrincipal(1L);
        var request = new OrderUpdateRequest(OrderStatus.CANCEL);

        doNothing().when(orderOwnerService).updateOrderStatus(eq(9001L), any(OrderUpdateRequest.class));

        mockMvc.perform(put("/api/v1/owner/orders/{orderId}", 9001L)
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void 주문_상태변경_실패() throws Exception {
        var auth = authPrincipal(1L);
        var request = new OrderUpdateRequest(OrderStatus.COMPLETED);

        doThrow(new OrderStatusInvalidUpdateException()).when(orderOwnerService).updateOrderStatus(eq(9001L), any(OrderUpdateRequest.class));

        mockMvc.perform(put("/api/v1/owner/orders/{orderId}", 9001L)
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
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
