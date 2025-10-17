package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.order.repository.OrderMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenuEntityFinder {
    private final OrderMenuRepository orderMenuRepository;

    @Transactional(readOnly = true)
    public List<OrderMenu> getAllOrderMenu(Long orderId) {
        return orderMenuRepository.findAllByOrderId(orderId);
    }
}
