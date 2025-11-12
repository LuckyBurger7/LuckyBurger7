import { useMemo, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useSearchParams } from 'react-router-dom';
import SectionHeader from '../components/SectionHeader';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorState from '../components/ErrorState';
import { fetchMenus } from '../api/queries';
import type { MenuResponse } from '../api/types';

const CATEGORY_LABEL: Record<MenuResponse['menuCategory'], string> = {
  HAMBURGER: '버거',
  SIDE: '사이드',
  DRINK: '음료'
};

function MenusPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const [page, setPage] = useState(() => Number(searchParams.get('page') ?? 0));
  const [keyword, setKeyword] = useState(searchParams.get('menuName') ?? '');

  const { data, isLoading, isError, error, refetch, isFetching } = useQuery({
    queryKey: ['menus', page, keyword],
    queryFn: () => fetchMenus({ page, size: 12, keyword: keyword || undefined }),
    keepPreviousData: true
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const value = (formData.get('keyword') as string).trim();
    setPage(0);
    setKeyword(value);
    setSearchParams((prev) => {
      const next = new URLSearchParams(prev);
      if (value) {
        next.set('menuName', value);
      } else {
        next.delete('menuName');
      }
      next.set('page', '0');
      return next;
    });
  };

  const hasNext = useMemo(() => (data ? data.page + 1 < data.totalPages : false), [data]);
  const hasPrev = page > 0;

  const handlePageChange = (nextPage: number) => {
    setPage(nextPage);
    setSearchParams((prev) => {
      const next = new URLSearchParams(prev);
      next.set('page', String(nextPage));
      return next;
    });
  };

  return (
    <div className="container">
      <section>
        <SectionHeader
          title="LuckyBurger7 메뉴"
          description="백엔드의 /api/v1/menus API와 검색 엔드포인트를 활용합니다"
        />
        <form
          onSubmit={handleSubmit}
          style={{ display: 'flex', gap: '1rem', marginBottom: '1.5rem', alignItems: 'center', flexWrap: 'wrap' }}
        >
          <input
            name="keyword"
            placeholder="메뉴 이름으로 검색"
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
              {data?.data.map((menu) => (
                <div key={menu.menuId} className="card">
                  <h3>{menu.name}</h3>
                  <p style={{ color: '#6b7280' }}>{CATEGORY_LABEL[menu.menuCategory]}</p>
                  <div style={{ fontWeight: 700, fontSize: '1.25rem' }}>{menu.price.toLocaleString()}원</div>
                  <p style={{ marginTop: '0.5rem', color: '#9ca3af' }}>ID: {menu.menuId}</p>
                </div>
              ))}
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '2rem' }}>
              <button type="button" className="tab-button" disabled={!hasPrev || isFetching} onClick={() => handlePageChange(page - 1)}>
                ← 이전
              </button>
              <div style={{ color: '#6b7280' }}>
                {data ? data.page + 1 : 0} / {data?.totalPages ?? 0}
              </div>
              <button type="button" className="tab-button" disabled={!hasNext || isFetching} onClick={() => handlePageChange(page + 1)}>
                다음 →
              </button>
            </div>
          </>
        )}
      </section>
    </div>
  );
}

export default MenusPage;
