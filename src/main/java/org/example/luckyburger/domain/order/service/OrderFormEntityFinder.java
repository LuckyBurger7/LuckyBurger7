package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.entity.OrderForm;
import org.example.luckyburger.domain.order.repository.OrderFormRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class OrderFormEntityFinder {
    private final OrderFormRepository orderFormRepository;

    public List<OrderForm> getAllOrderFormByUser(User user) {
        return orderFormRepository.findAllByUser(user);
    }
}
