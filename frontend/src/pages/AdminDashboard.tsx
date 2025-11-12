import { useMemo } from 'react';
import { useQuery } from '@tanstack/react-query';
import SectionHeader from '../components/SectionHeader';
import DataCard from '../components/DataCard';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorState from '../components/ErrorState';
import {
  fetchAdminDashboard,
  fetchBottomShops,
  fetchBurgerSales,
  fetchMonthlySales,
  fetchSideSales,
  fetchTopShops
} from '../api/queries';
import { useAuth } from '../context/AuthContext';

function AdminDashboardPage() {
  const { isAuthenticated } = useAuth();

  const dashboardQuery = useQuery({
    queryKey: ['admin', 'dashboard'],
    queryFn: fetchAdminDashboard,
    enabled: isAuthenticated
  });

  const monthSalesQuery = useQuery({
    queryKey: ['admin', 'sales', 'monthly'],
    queryFn: fetchMonthlySales,
    enabled: isAuthenticated
  });

  const topShopQuery = useQuery({
    queryKey: ['admin', 'shops', 'top'],
    queryFn: fetchTopShops,
    enabled: isAuthenticated
  });

  const bottomShopQuery = useQuery({
    queryKey: ['admin', 'shops', 'bottom'],
    queryFn: fetchBottomShops,
    enabled: isAuthenticated
  });

  const burgerSalesQuery = useQuery({
    queryKey: ['admin', 'menus', 'burger'],
    queryFn: fetchBurgerSales,
    enabled: isAuthenticated
  });

  const sideSalesQuery = useQuery({
    queryKey: ['admin', 'menus', 'side'],
    queryFn: fetchSideSales,
    enabled: isAuthenticated
  });

  const monthlyMax = useMemo(() => {
    if (!monthSalesQuery.data || monthSalesQuery.data.length === 0) {
      return 0;
    }
    return Math.max(...monthSalesQuery.data.map((item) => item.totalSales));
  }, [monthSalesQuery.data]);

  if (!isAuthenticated) {
    return (
      <div className="container">
        <section>
          <SectionHeader
            title="관리자 대시보드"
            description="/api/v1/admin/** 엔드포인트는 ADMIN 권한 JWT 토큰이 필요합니다"
          />
          <div className="alert">관리자 계정으로 로그인하면 매출 및 매장 통계를 확인할 수 있습니다.</div>
        </section>
      </div>
    );
  }

  return (
    <div className="container">
      <section>
        <SectionHeader title="관리자 대시보드" description="매출 · 주문 · 쿠폰 등 핵심 지표를 집계합니다" />
        {dashboardQuery.isLoading ? (
          <LoadingSpinner />
        ) : dashboardQuery.isError ? (
          <ErrorState message={(dashboardQuery.error as Error).message} retry={dashboardQuery.refetch} />
        ) : dashboardQuery.data ? (
          <div className="grid grid-3">
            <DataCard title="전체 매장" value={`${dashboardQuery.data.totalShopCount.toLocaleString()}곳`} />
            <DataCard title="누적 주문" value={`${dashboardQuery.data.totalOrderCount.toLocaleString()}건`} />
            <DataCard
              title="누적 매출"
              value={`${dashboardQuery.data.totalSales.toLocaleString()}원`}
              subtitle={`발급된 쿠폰 ${dashboardQuery.data.activeCoupon.toLocaleString()}개`}
            />
          </div>
        ) : null}
      </section>

      <section>
        <SectionHeader title="월별 매출" description="/api/v1/admin/statistics/sales/monthly" />
        {monthSalesQuery.isLoading ? (
          <LoadingSpinner />
        ) : monthSalesQuery.isError ? (
          <ErrorState message={(monthSalesQuery.error as Error).message} retry={monthSalesQuery.refetch} />
        ) : (
          <div className="card" style={{ display: 'grid', gap: '1rem' }}>
            {monthSalesQuery.data?.map((item) => {
              const ratio = monthlyMax ? item.totalSales / monthlyMax : 0;
              return (
                <div key={`${item.year}-${item.month}`}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.25rem' }}>
                    <strong>
                      {item.year}.{String(item.month).padStart(2, '0')}
                    </strong>
                    <span>{item.totalSales.toLocaleString()}원</span>
                  </div>
                  <div style={{ background: '#f3f4f6', borderRadius: '999px', overflow: 'hidden' }}>
                    <div
                      style={{
                        width: `${Math.max(ratio * 100, 4)}%`,
                        background: 'linear-gradient(90deg,#f97316,#fb923c)',
                        height: '0.75rem'
                      }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </section>

      <section>
        <SectionHeader title="매장 매출 TOP 10" description="/api/v1/admin/statistics/sales/shops/top10" />
        {topShopQuery.isLoading ? (
          <LoadingSpinner />
        ) : topShopQuery.isError ? (
          <ErrorState message={(topShopQuery.error as Error).message} retry={topShopQuery.refetch} />
        ) : (
          <div className="card" style={{ display: 'grid', gap: '0.75rem' }}>
            {topShopQuery.data?.map((shop, index) => (
              <div key={shop.shopId} style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>
                  #{index + 1} {shop.shopName}
                </span>
                <strong>{shop.totalSales.toLocaleString()}원</strong>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <SectionHeader title="매장 매출 BOTTOM 10" description="/api/v1/admin/statistics/sales/shops/bottom10" />
        {bottomShopQuery.isLoading ? (
          <LoadingSpinner />
        ) : bottomShopQuery.isError ? (
          <ErrorState message={(bottomShopQuery.error as Error).message} retry={bottomShopQuery.refetch} />
        ) : (
          <div className="card" style={{ display: 'grid', gap: '0.75rem' }}>
            {bottomShopQuery.data?.map((shop, index) => (
              <div key={shop.shopId} style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span>
                  #{index + 1} {shop.shopName}
                </span>
                <strong>{shop.totalSales.toLocaleString()}원</strong>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <SectionHeader title="카테고리별 인기 메뉴" description="버거/사이드 매출 TOP" />
        <div className="grid grid-3">
          <div className="card">
            <h3>버거</h3>
            {burgerSalesQuery.isLoading ? (
              <LoadingSpinner />
            ) : burgerSalesQuery.isError ? (
              <ErrorState message={(burgerSalesQuery.error as Error).message} retry={burgerSalesQuery.refetch} />
            ) : (
              <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'grid', gap: '0.5rem' }}>
                {burgerSalesQuery.data?.map((menu, index) => (
                  <li key={menu.menuId} style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>
                      #{index + 1} {menu.menuName}
                    </span>
                    <strong>{menu.totalSales.toLocaleString()}원</strong>
                  </li>
                ))}
              </ul>
            )}
          </div>
          <div className="card">
            <h3>사이드</h3>
            {sideSalesQuery.isLoading ? (
              <LoadingSpinner />
            ) : sideSalesQuery.isError ? (
              <ErrorState message={(sideSalesQuery.error as Error).message} retry={sideSalesQuery.refetch} />
            ) : (
              <ul style={{ listStyle: 'none', padding: 0, margin: 0, display: 'grid', gap: '0.5rem' }}>
                {sideSalesQuery.data?.map((menu, index) => (
                  <li key={menu.menuId} style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>
                      #{index + 1} {menu.menuName}
                    </span>
                    <strong>{menu.totalSales.toLocaleString()}원</strong>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </section>
    </div>
  );
}

export default AdminDashboardPage;
