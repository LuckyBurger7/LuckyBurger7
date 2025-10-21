package org.example.luckyburger.domain.order.controller;

import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.filter.JwtAuthenticationFilter;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.order.dto.response.OrderCountResponse;
import org.example.luckyburger.domain.order.service.OrderAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("local")
public class OrderAdminControllerTest {
    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderAdminService orderAdminService;
    @MockitoBean
    private JwtUtil jwtUtil;

    private TestingAuthenticationToken authPrincipal(long accountId) {
        AuthAccount principal = mock(AuthAccount.class);
        when(principal.getAccountId()).thenReturn(accountId);
        return new TestingAuthenticationToken(principal, null);
    }

    @Test
    void 총_주문수_조회_성공() throws Exception {
        var auth = authPrincipal(1L);
        var response = OrderCountResponse.of(1);

        given(orderAdminService.getOrderCountResponse())
                .willReturn(response);

        mockMvc.perform(get("/api/v1/admin/orders/count")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.count").value(1L));
    }
}
