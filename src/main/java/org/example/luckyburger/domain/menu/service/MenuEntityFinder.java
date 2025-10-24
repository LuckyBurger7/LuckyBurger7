package org.example.luckyburger.domain.menu.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.exception.MenuNotFoundException;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class MenuEntityFinder {

    private final MenuRepository menuRepository;

    public Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }

    public List<Long> getAllMenuIdByCategory(MenuCategory category) {
        return menuRepository.findAllByCategory(category);
    }
}
