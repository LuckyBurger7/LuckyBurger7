import { useMemo, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import SectionHeader from '../components/SectionHeader';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorState from '../components/ErrorState';
import { fetchCoupons } from '../api/queries';
import type { CouponResponse } from '../api/types';

function formatCoupon(coupon: CouponResponse) {
  if (coupon.couponType === 'RATIO') {
    return `${coupon.discount}% 할인`;
  }
  return `${coupon.discount.toLocaleString()}원 할인`;
}

function CouponsPage() {
  const [page, setPage] = useState(0);
  const { data, isLoading, isError, error, refetch, isFetching } = useQuery({
    queryKey: ['coupons', page],
    queryFn: () => fetchCoupons(page, 12),
    keepPreviousData: true
  });

  const hasNext = useMemo(() => (data ? data.page + 1 < data.totalPages : false), [data]);
  const hasPrev = page > 0;

  return (
    <div className="container">
      <section>
        <SectionHeader
          title="LuckyBurger7 쿠폰"
          description="/api/v1/coupons API로부터 전체 쿠폰 리스트를 받아옵니다"
        />
        {isLoading ? (
          <LoadingSpinner />
        ) : isError ? (
          <ErrorState message={(error as Error).message} retry={refetch} />
        ) : (
          <>
            <div className="table-wrapper">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>쿠폰명</th>
                    <th>혜택</th>
                    <th>남은 수량</th>
                    <th>유효기간</th>
                    <th>생성일</th>
                  </tr>
                </thead>
                <tbody>
                  {data?.data.map((coupon) => (
                    <tr key={coupon.couponId}>
                      <td>{coupon.couponId}</td>
                      <td>{coupon.name}</td>
                      <td>{formatCoupon(coupon)}</td>
                      <td>{coupon.count}</td>
                      <td>{new Date(coupon.expirationDate).toLocaleDateString()}</td>
                      <td>{new Date(coupon.createAt).toLocaleDateString()}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
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
    </div>
  );
}

export default CouponsPage;
