package org.example.luckyburger.domain.menu.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.exception.NotFoundMenuException;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntityFinder {

    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NotFoundMenuException::new);
    }
}
