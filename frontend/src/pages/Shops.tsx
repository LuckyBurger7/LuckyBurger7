import { useMemo, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import SectionHeader from '../components/SectionHeader';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorState from '../components/ErrorState';
import { fetchShops, fetchShopMenus } from '../api/queries';
import type { BusinessStatus, ShopMenuResponse } from '../api/types';
import type { ApiPageResponse } from '../api/client';

const STATUS_LABEL: Record<BusinessStatus, string> = {
  OPEN: '영업중',
  BREAK: '브레이크 타임',
  CLOSED: '영업종료'
};

const STATUS_CLASS: Record<BusinessStatus, string> = {
  OPEN: 'status-open',
  BREAK: 'status-break',
  CLOSED: 'status-closed'
};

const MENU_CATEGORY_LABEL: Record<ShopMenuResponse['menuCategory'], string> = {
  HAMBURGER: '버거',
  SIDE: '사이드',
  DRINK: '음료'
};

function ShopsPage() {
  const [keyword, setKeyword] = useState('');
  const [page, setPage] = useState(0);
  const [selectedShop, setSelectedShop] = useState<number | null>(null);

  const {
    data,
    isLoading,
    isError,
    error,
    refetch,
    isFetching
  } = useQuery({
    queryKey: ['shops', page, keyword],
    queryFn: () => fetchShops({ page, size: 10, keyword: keyword || undefined }),
    keepPreviousData: true
  });

  const {
    data: menuPage,
    isFetching: isMenuFetching,
    error: menuError,
    refetch: refetchMenus
  } = useQuery<ApiPageResponse<ShopMenuResponse>>({
    queryKey: ['shopMenus', selectedShop],
    queryFn: () => fetchShopMenus(selectedShop!, 0, 6),
    enabled: selectedShop !== null
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    setKeyword((formData.get('keyword') as string).trim());
    setPage(0);
  };

  const hasNext = useMemo(() => (data ? data.page + 1 < data.totalPages : false), [data]);
  const hasPrev = page > 0;

  return (
    <div className="container">
      <section>
        <SectionHeader
          title="LuckyBurger7 매장"
          description="/api/v1/shops/search 및 매장별 메뉴 조회 엔드포인트를 연결했습니다"
        />
        <form
          onSubmit={handleSubmit}
          style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem', flexWrap: 'wrap', alignItems: 'center' }}
        >
          <input
            name="keyword"
            placeholder="매장 이름으로 검색"
            defaultValue={keyword}
            style={{ flex: '1 1 260px' }}
          />
          <button type="submit" className="primary" disabled={isFetching}>
            검색
          </button>
        </form>
        {isLoading ? (
          <LoadingSpinner />
        ) : isError ? (
          <ErrorState message={(error as Error).message} retry={refetch} />
        ) : (
          <>
            <div className="grid grid-3">
              {data?.data.map((shop) => (
                <div key={shop.shopId} className="card">
                  <h3>{shop.name}</h3>
                  <p style={{ color: '#6b7280' }}>{shop.address}</p>
                  <div className={STATUS_CLASS[shop.businessStatus]}>{STATUS_LABEL[shop.businessStatus]}</div>
                  <button
                    type="button"
                    className="tab-button"
                    style={{ marginTop: '1rem' }}
                    onClick={() => setSelectedShop(shop.shopId)}
                  >
                    메뉴 보기
                  </button>
                </div>
              ))}
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '2rem' }}>
              <button type="button" className="tab-button" disabled={!hasPrev || isFetching} onClick={() => setPage((prev) => prev - 1)}>
                ← 이전
              </button>
              <div style={{ color: '#6b7280' }}>
                {data ? data.page + 1 : 0} / {data?.totalPages ?? 0}
              </div>
              <button type="button" className="tab-button" disabled={!hasNext || isFetching} onClick={() => setPage((prev) => prev + 1)}>
                다음 →
              </button>
            </div>
          </>
        )}
      </section>

      {selectedShop ? (
        <section>
          <SectionHeader
            title={`선택한 매장 (#${selectedShop}) 메뉴`}
            description="/api/v1/shops/{shopId}/shopMenus 응답을 보여줍니다"
            action={
              <button type="button" className="tab-button" onClick={() => setSelectedShop(null)}>
                닫기
              </button>
            }
          />
          {isMenuFetching ? (
            <LoadingSpinner />
          ) : menuError ? (
            <ErrorState message={(menuError as Error).message} retry={refetchMenus} />
          ) : (
            <div className="grid grid-3">
              {menuPage?.data.map((menu: ShopMenuResponse) => (
                <div key={menu.shopMenuId} className="card">
                  <h3>{menu.name}</h3>
                  <p style={{ color: '#6b7280' }}>{MENU_CATEGORY_LABEL[menu.menuCategory]}</p>
                  <div style={{ fontWeight: 700 }}>{menu.price.toLocaleString()}원</div>
                  <span style={{ color: '#f97316', fontWeight: 600 }}>{menu.menuStatus}</span>
                </div>
              ))}
            </div>
          )}
        </section>
      ) : null}
    </div>
  );
}

export default ShopsPage;
