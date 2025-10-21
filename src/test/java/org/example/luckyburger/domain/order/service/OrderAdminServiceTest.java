package org.example.luckyburger.domain.order.service;

import org.example.luckyburger.domain.order.dto.response.OrderCountResponse;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderAdminServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderAdminService orderAdminService;

    @Test
    void 총_주문수_조회_성공() {
        // given
        when(orderRepository.countByStatusNot(OrderStatus.CANCEL)).thenReturn(1L);

        //when
        OrderCountResponse response = orderAdminService.getOrderCountResponse();

        //then
        assertThat(response.count()).isEqualTo(1L);
    }
}
