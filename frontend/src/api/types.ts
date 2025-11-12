export type MenuCategory = 'HAMBURGER' | 'SIDE' | 'DRINK';

export interface MenuResponse {
  menuId: number;
  name: string;
  menuCategory: MenuCategory;
  price: number;
}

export type BusinessStatus = 'OPEN' | 'BREAK' | 'CLOSED';

export interface ShopResponse {
  shopId: number;
  name: string;
  businessStatus: BusinessStatus;
  address: string;
  street: string;
}

export type ShopMenuStatus = 'ON_SALE' | 'OUT_OF_STOCK' | 'DEACTIVATE';

export interface ShopMenuResponse {
  shopMenuId: number;
  name: string;
  menuCategory: MenuCategory;
  price: number;
  menuStatus: ShopMenuStatus;
}

export type CouponType = 'RATIO' | 'FIXED';

export interface CouponResponse {
  couponId: number;
  name: string;
  discount: number;
  count: number;
  expirationDate: string;
  couponType: CouponType;
  createAt: string;
}

export interface CartMenuResponse {
  shopMenuId: number;
  menuName: string;
  shopName: string;
  quantity: number;
  price: number;
}

export interface CartResponse {
  cartId: number;
  cartMenus: CartMenuResponse[];
  totalPrice: number;
}

export interface TokenResponse {
  accessToken: string;
}

export interface AdminDashboardResponse {
  totalShopCount: number;
  totalOrderCount: number;
  activeCoupon: number;
  totalSales: number;
}

export interface MonthTotalSalesResponse {
  year: number;
  month: number;
  totalSales: number;
}

export interface ShopTotalSalesResponse {
  shopId: number;
  shopName: string;
  totalSales: number;
}

export interface MenuTotalSalesResponse {
  menuId: number;
  menuName: string;
  totalSales: number;
}
