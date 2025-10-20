package org.example.luckyburger.domain.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.luckyburger.common.security.properties.JwtSecurityProperties;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
import org.example.luckyburger.domain.menu.dto.request.MenuUpdateRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.exception.MenuNotFoundException;
import org.example.luckyburger.domain.menu.service.MenuAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuAdminController.class)
@ActiveProfiles("local")
public class MenuAdminControllerTest {

    private static final String menuName = "햄버거";
    private static final MenuCategory category = MenuCategory.HAMBURGER;
    private static final long price = 8000;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private MenuAdminService menuAdminService;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private JwtSecurityProperties jwtSecurityProperties;

    @BeforeEach
    void setUp() {
        when(jwtSecurityProperties.getSecret())
                .thenReturn(new JwtSecurityProperties.Secret("dummyAesKey", "dummyKey", List.of("/api/v1/admin/menus", "/api/v1/admin/menus/{menuId}")));
    }

    @Test
    @WithMockUser
    void 메뉴_생성에_성공한다() throws Exception {

        // given
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest(menuName, category, price);

        Long menuId = 1L;
        Menu menu = Menu.of(menuName, category, price);

        ReflectionTestUtils.setField(menu, "id", menuId);

        MenuResponse response = MenuResponse.from(menu);
        when(menuAdminService.createMenu(menuCreateRequest)).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/admin/menus")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(menuCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(menuId))
                .andExpect(jsonPath("$.data.name").value(menuName))
                .andExpect(jsonPath("$.data.menuCategory").value(category.name()))
                .andExpect(jsonPath("$.data.price").value(price));

        System.out.println();
    }

    @Test
    @WithMockUser
    void 메뉴_수정에_성공한다() throws Exception {

        // given
        MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest(menuName, category, price);

        Long menuId = 1L;
        Menu updatedMenu = Menu.of(menuName, category, price);
        ReflectionTestUtils.setField(updatedMenu, "id", menuId);

        MenuResponse response = MenuResponse.from(updatedMenu);
        when(menuAdminService.updateMenu(menuId, menuUpdateRequest)).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/v1/admin/menus/{menuId}", menuId)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(menuUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(menuId))
                .andExpect(jsonPath("$.data.name").value(menuName))
                .andExpect(jsonPath("$.data.menuCategory").value(category.name()))
                .andExpect(jsonPath("$.data.price").value(price));

    }

    @Test
    @WithMockUser
    void 메뉴가_존재하지_않아_수정에_실패한다() throws Exception {
        // given
        MenuUpdateRequest menuUpdateRequest = new MenuUpdateRequest(menuName, category, price);

        Long menuId = 1L;
        when(menuAdminService.updateMenu(menuId, menuUpdateRequest)).thenThrow(new MenuNotFoundException());

        mockMvc.perform(put("/api/v1/admin/menus/{menuId}", menuId)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(menuUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void 메뉴_삭제에_성공한다() throws Exception {
        Long menuId = 1L;
        doNothing().when(menuAdminService).deleteMenu(menuId);

        mockMvc.perform(delete("/api/v1/admin/menus/{menuId}", menuId)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 메뉴가_존재하지_않아_삭제에_실패한다() throws Exception {
        Long menuId = 1L;
        doThrow(new MenuNotFoundException()).when(menuAdminService).deleteMenu(menuId);

        mockMvc.perform(delete("/api/v1/admin/menus/{menuId}", menuId)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

}
