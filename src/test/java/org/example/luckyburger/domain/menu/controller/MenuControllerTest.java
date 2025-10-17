package org.example.luckyburger.domain.menu.controller;

import org.example.luckyburger.common.security.filter.JwtAuthenticationFilter;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MenuController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("local")
class MenuControllerTest {

    private static final String menuName = "햄버거";
    private static final MenuCategory category = MenuCategory.HAMBURGER;
    private static final long price = 8000;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void 메뉴_단일조회_성공() throws Exception {
        Long menuId = 1L;
        Menu menu = Menu.of(menuName, category, price);

        ReflectionTestUtils.setField(menu, "id", menuId);

        MenuResponse response = MenuResponse.from(menu);

        given(menuService.getMenuResponse(menuId)).willReturn(response);

        mockMvc.perform(get("/api/v1/menus/{menuId}", menuId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(menuId))
                .andExpect(jsonPath("$.data.name").value(menuName));
    }

    @Test
    void 메뉴_전체조회_성공() throws Exception {
        MenuResponse menu1 = new MenuResponse(1L, "햄버거", null, 5000);
        MenuResponse menu2 = new MenuResponse(2L, "치킨버거", null, 6000);
        Page<MenuResponse> pageResponse = new PageImpl<>(List.of(menu1, menu2), PageRequest.of(0, 10), 2);

        given(menuService.getAllMenuResponse(any(Pageable.class))).willReturn(pageResponse);

        mockMvc.perform(get("/api/v1/menus")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("햄버거"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("치킨버거"));
    }
}

