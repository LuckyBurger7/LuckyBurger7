import { useQuery } from '@tanstack/react-query';
import { Link } from 'react-router-dom';
import SectionHeader from '../components/SectionHeader';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorState from '../components/ErrorState';
import DataCard from '../components/DataCard';
import { fetchMenus, fetchCoupons, fetchShops } from '../api/queries';
import type { MenuResponse } from '../api/types';

const MENU_LABEL: Record<MenuResponse['menuCategory'], string> = {
  HAMBURGER: '버거',
  SIDE: '사이드',
  DRINK: '음료'
};

function HomePage() {
  const {
    data: menuPage,
    isLoading: isMenuLoading,
    isError: isMenuError,
    error: menuError,
    refetch: refetchMenu
  } = useQuery({
    queryKey: ['menus', 'home'],
    queryFn: () => fetchMenus({ page: 0, size: 6 })
  });

  const {
    data: couponPage,
    isLoading: isCouponLoading,
    isError: isCouponError,
    error: couponError,
    refetch: refetchCoupons
  } = useQuery({
    queryKey: ['coupons', 'home'],
    queryFn: () => fetchCoupons(0, 4)
  });

  const {
    data: shopPage,
    isLoading: isShopLoading,
    isError: isShopError,
    error: shopError,
    refetch: refetchShops
  } = useQuery({
    queryKey: ['shops', 'home'],
    queryFn: () => fetchShops({ page: 0, size: 4 })
  });

  return (
    <div className="container">
      <section className="hero">
        <div>
          <span className="badge">LuckyBurger7 Platform</span>
          <h1>
            고성능 버거 주문 & 통계 플랫폼
            <br /> 프론트엔드 대시보드
          </h1>
          <p>
            Spring Boot 백엔드가 제공하는 다양한 API를 탐색하고, 매장 · 메뉴 · 쿠폰 · 장바구니 기능을 하나의
            프론트에서 빠르게 테스트해 보세요.
          </p>
          <div style={{ display: 'flex', gap: '1rem', marginTop: '2rem', flexWrap: 'wrap' }}>
            <Link to="/menus">
              <button type="button" className="primary">
                전체 메뉴 보기
              </button>
            </Link>
            <Link to="/admin" style={{ color: '#f97316', fontWeight: 600 }}>
              관리자 지표 살펴보기 →
            </Link>
          </div>
        </div>
        <div>
          <div
            style={{
              background: 'linear-gradient(135deg, #f97316, #ea580c)',
              borderRadius: '1.5rem',
              padding: '3rem',
              color: 'white',
              boxShadow: '0 40px 70px -50px rgba(249, 115, 22, 0.8)'
            }}
          >
            <h2 style={{ margin: 0 }}>Lucky Insights</h2>
            <p style={{ color: 'rgba(255,255,255,0.8)' }}>
              실시간 통계와 버거 매장의 성장을 위한 핵심 데이터를 시각화합니다.
            </p>
            <ul style={{ listStyle: 'none', margin: '2rem 0 0', padding: 0, display: 'grid', gap: '1rem' }}>
              <li>✅ 전국 매장 재고 및 영업상태 파악</li>
              <li>✅ 인기 메뉴/쿠폰 실적 추적</li>
              <li>✅ 주문 처리량 및 매출 추세 분석</li>
            </ul>
          </div>
        </div>
      </section>

      <section>
        <SectionHeader title="API 요약" description="LuckyBurger7 백엔드에서 제공하는 주요 엔드포인트" />
        <div className="grid grid-3">
          <DataCard
            title="메뉴"
            value={`${menuPage?.totalElements ?? 0} 개`}
            subtitle="카테고리별 판매 메뉴를 조회하고 검색하세요"
          />
          <DataCard title="매장" value={`${shopPage?.totalElements ?? 0} 개`} subtitle="전국 LuckyBurger 매장을 탐색" />
          <DataCard
            title="쿠폰"
            value={`${couponPage?.totalElements ?? 0} 종`}
            subtitle="발급 가능한 프로모션 현황"
          />
        </div>
      </section>

      <section>
        <SectionHeader title="추천 메뉴" description="최근 등록된 인기 메뉴를 빠르게 확인하세요" />
        {isMenuLoading ? (
          <LoadingSpinner />
        ) : isMenuError ? (
          <ErrorState message={(menuError as Error).message} retry={refetchMenu} />
        ) : (
          <div className="grid grid-3">
            {menuPage?.data.map((menu) => (
              <div key={menu.menuId} className="card">
                <h3>{menu.name}</h3>
                <p style={{ color: '#6b7280' }}>{MENU_LABEL[menu.menuCategory]}</p>
                <div style={{ fontWeight: 700, fontSize: '1.25rem' }}>{menu.price.toLocaleString()}원</div>
                <Link to={`/menus?menuId=${menu.menuId}`} style={{ color: '#f97316', fontWeight: 600 }}>
                  자세히 보기 →
                </Link>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <SectionHeader title="가까운 매장" description="LuckyBurger7에서 운영 중인 매장을 검색해 보세요" />
        {isShopLoading ? (
          <LoadingSpinner />
        ) : isShopError ? (
          <ErrorState message={(shopError as Error).message} retry={refetchShops} />
        ) : (
          <div className="grid grid-3">
            {shopPage?.data.map((shop) => (
              <div key={shop.shopId} className="card">
                <h3>{shop.name}</h3>
                <p style={{ color: '#6b7280' }}>{shop.address}</p>
                <span className={`status-${shop.businessStatus.toLowerCase()}`}>{shop.businessStatus}</span>
              </div>
            ))}
          </div>
        )}
      </section>

      <section>
        <SectionHeader title="이달의 쿠폰" description="할인 혜택을 놓치지 마세요" />
        {isCouponLoading ? (
          <LoadingSpinner />
        ) : isCouponError ? (
          <ErrorState message={(couponError as Error).message} retry={refetchCoupons} />
        ) : (
          <div className="grid grid-3">
            {couponPage?.data.map((coupon) => (
              <div key={coupon.couponId} className="card">
                <h3>{coupon.name}</h3>
                <p style={{ color: '#6b7280' }}>{coupon.couponType}</p>
                <div style={{ fontWeight: 700, fontSize: '1.1rem' }}>
                  할인율 {coupon.discount}% · 재고 {coupon.count}개
                </div>
                <div style={{ marginTop: '0.5rem', color: '#9ca3af' }}>
                  {new Date(coupon.expirationDate).toLocaleDateString()}
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

export default HomePage;
