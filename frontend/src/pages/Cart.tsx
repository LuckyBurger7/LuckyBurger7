import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import SectionHeader from '../components/SectionHeader';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorState from '../components/ErrorState';
import {
  addCartMenu,
  deleteCartMenu,
  fetchCart,
  updateCartMenu
} from '../api/queries';
import type { CartResponse } from '../api/types';
import { useAuth } from '../context/AuthContext';

function CartPage() {
  const queryClient = useQueryClient();
  const { isAuthenticated } = useAuth();
  const [useCache, setUseCache] = useState(false);
  const [addId, setAddId] = useState('');
  const [updatePayload, setUpdatePayload] = useState({ shopMenuId: '', quantity: 1 });

  const {
    data,
    isLoading,
    isError,
    error,
    refetch
  } = useQuery<CartResponse>({
    queryKey: ['cart', useCache],
    queryFn: () => fetchCart(useCache),
    enabled: isAuthenticated
  });

  const addMutation = useMutation({
    mutationFn: (shopMenuId: number) => addCartMenu(shopMenuId, useCache),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart', useCache] });
      setAddId('');
    }
  });

  const updateMutation = useMutation({
    mutationFn: ({ shopMenuId, quantity }: { shopMenuId: number; quantity: number }) =>
      updateCartMenu(shopMenuId, quantity, useCache),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart', useCache] });
    }
  });

  const deleteMutation = useMutation({
    mutationFn: (shopMenuId: number) => deleteCartMenu(shopMenuId, useCache),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cart', useCache] });
    }
  });

  const handleAdd = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const id = Number(addId);
    if (!id || Number.isNaN(id)) return;
    addMutation.mutate(id);
  };

  const handleUpdate = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const id = Number(updatePayload.shopMenuId);
    const quantity = Number(updatePayload.quantity);
    if (!id || Number.isNaN(id) || quantity <= 0) return;
    updateMutation.mutate({ shopMenuId: id, quantity });
  };

  if (!isAuthenticated) {
    return (
      <div className="container">
        <section>
          <SectionHeader
            title="장바구니"
            description="/api/v1/user/carts 및 /api/v2/user/carts API는 인증 토큰이 필요합니다"
          />
          <div className="alert">먼저 로그인하여 JWT 액세스 토큰을 발급받은 뒤 이용해 주세요.</div>
        </section>
      </div>
    );
  }

  return (
    <div className="container">
      <section>
        <SectionHeader
          title="장바구니"
          description={useCache ? 'Redis 캐시 버전(v2) API를 호출합니다' : 'RDB 기반 기본(v1) API를 호출합니다'}
          action={
            <button type="button" className="tab-button" onClick={() => setUseCache((prev) => !prev)}>
              {useCache ? 'v1 호출로 전환' : 'v2 호출로 전환'}
            </button>
          }
        />
        {isLoading ? (
          <LoadingSpinner />
        ) : isError ? (
          <ErrorState message={(error as Error).message} retry={refetch} />
        ) : (
          <div className="card">
            <h3>총 금액</h3>
            <div style={{ fontSize: '2rem', fontWeight: 700 }}>{data?.totalPrice.toLocaleString()}원</div>
            <div style={{ marginTop: '1.5rem', display: 'grid', gap: '1rem' }}>
              {data?.cartMenus.map((item) => (
                <div key={item.shopMenuId} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <div style={{ fontWeight: 600 }}>{item.menuName}</div>
                    <div style={{ color: '#6b7280', fontSize: '0.9rem' }}>{item.shopName}</div>
                    <div style={{ marginTop: '0.25rem' }}>{item.price.toLocaleString()}원 × {item.quantity}</div>
                  </div>
                  <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
                    <button
                      type="button"
                      className="tab-button"
                      onClick={() => deleteMutation.mutate(item.shopMenuId)}
                      disabled={deleteMutation.isPending}
                    >
                      삭제
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </section>

      <section>
        <SectionHeader title="메뉴 추가" description="/api/v1/user/carts POST" />
        <form onSubmit={handleAdd} className="card" style={{ display: 'grid', gap: '1rem' }}>
          <label>
            <span>Shop Menu ID</span>
            <input value={addId} onChange={(event) => setAddId(event.target.value)} placeholder="숫자 ID" />
          </label>
          <button type="submit" className="primary" disabled={addMutation.isPending}>
            장바구니에 추가
          </button>
          {addMutation.error ? <div className="alert">{(addMutation.error as Error).message}</div> : null}
        </form>
      </section>

      <section>
        <SectionHeader title="수량 변경" description="/api/v1/user/carts PUT" />
        <form onSubmit={handleUpdate} className="card" style={{ display: 'grid', gap: '1rem' }}>
          <label>
            <span>Shop Menu ID</span>
            <input
              value={updatePayload.shopMenuId}
              onChange={(event) => setUpdatePayload((prev) => ({ ...prev, shopMenuId: event.target.value }))}
              placeholder="숫자 ID"
            />
          </label>
          <label>
            <span>수량</span>
            <input
              type="number"
              min={1}
              value={updatePayload.quantity}
              onChange={(event) => setUpdatePayload((prev) => ({ ...prev, quantity: Number(event.target.value) }))}
            />
          </label>
          <button type="submit" className="primary" disabled={updateMutation.isPending}>
            수량 업데이트
          </button>
          {updateMutation.error ? <div className="alert">{(updateMutation.error as Error).message}</div> : null}
        </form>
      </section>
    </div>
  );
}

export default CartPage;
