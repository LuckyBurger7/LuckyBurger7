import {
  AdminDashboardResponse,
  CartResponse,
  CouponResponse,
  MenuResponse,
  MenuTotalSalesResponse,
  MonthTotalSalesResponse,
  ShopMenuResponse,
  ShopResponse,
  ShopTotalSalesResponse,
  TokenResponse
} from './types';
import { ApiResponse, getData, getPagedData, apiPost, apiDelete, apiPut } from './client';

export function fetchMenus({ page = 0, size = 12, keyword }: { page?: number; size?: number; keyword?: string }) {
  const basePath = keyword ? `/v1/menus/search?menuName=${encodeURIComponent(keyword)}` : '/v1/menus';
  return getPagedData<MenuResponse>(`${basePath}${basePath.includes('?') ? '&' : '?'}page=${page}&size=${size}`);
}

export function fetchMenuDetail(menuId: number) {
  return getData<MenuResponse>(`/v1/menus/${menuId}`);
}

export function fetchShops({ page = 0, size = 12, keyword }: { page?: number; size?: number; keyword?: string }) {
  const query = new URLSearchParams();
  if (keyword) {
    query.set('shopName', keyword);
  }
  query.set('page', String(page));
  query.set('size', String(size));
  return getPagedData<ShopResponse>(`/v1/shops/search?${query.toString()}`);
}

export function fetchShopMenus(shopId: number, page = 0, size = 12) {
  return getPagedData<ShopMenuResponse>(`/v1/shops/${shopId}/shopMenus?page=${page}&size=${size}`);
}

export function fetchCoupons(page = 0, size = 12) {
  return getPagedData<CouponResponse>(`/v1/coupons?page=${page}&size=${size}`);
}

export function fetchCart(useCache = false) {
  return getData<CartResponse>(useCache ? '/v2/user/carts' : '/v1/user/carts');
}

export function addCartMenu(shopMenuId: number, useCache = false) {
  return apiPost<ApiResponse<void>>(useCache ? '/v2/user/carts' : '/v1/user/carts', {
    shopMenuId
  });
}

export function updateCartMenu(shopMenuId: number, quantity: number, useCache = false) {
  return apiPut<ApiResponse<CartResponse>>(useCache ? '/v2/user/carts' : '/v1/user/carts', {
    shopMenuId,
    quantity
  });
}

export function deleteCartMenu(shopMenuId: number, useCache = false) {
  return apiDelete<ApiResponse<CartResponse>>(useCache ? '/v2/user/carts' : '/v1/user/carts', {
    shopMenuId
  });
}

export function authenticate(loginForm: { email: string; password: string }) {
  return apiPost<ApiResponse<TokenResponse>>('/v1/login', loginForm).then((response) => response.data);
}

export function fetchAdminDashboard() {
  return getData<AdminDashboardResponse>('/v1/admin/dashboard');
}

export function fetchMonthlySales() {
  return getData<MonthTotalSalesResponse[]>('/v1/admin/statistics/sales/monthly');
}

export function fetchTopShops() {
  return getData<ShopTotalSalesResponse[]>('/v1/admin/statistics/sales/shops/top10');
}

export function fetchBottomShops() {
  return getData<ShopTotalSalesResponse[]>('/v1/admin/statistics/sales/shops/bottom10');
}

export function fetchBurgerSales() {
  return getData<MenuTotalSalesResponse[]>('/v1/admin/statistics/sales/menus/burger');
}

export function fetchSideSales() {
  return getData<MenuTotalSalesResponse[]>('/v1/admin/statistics/sales/menus/side');
}
