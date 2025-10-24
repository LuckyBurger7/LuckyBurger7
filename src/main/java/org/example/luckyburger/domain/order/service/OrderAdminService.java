package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.dto.response.OrderCountResponse;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderAdminService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderCountResponse getOrderCountResponse() {
        long count = orderRepository.countByStatusNot(OrderStatus.CANCEL);
        return OrderCountResponse.of(count);
    }
}


