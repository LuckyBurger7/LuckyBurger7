import { useState } from 'react';
import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import SectionHeader from '../components/SectionHeader';
import { authenticate } from '../api/queries';
import { useAuth } from '../context/AuthContext';

function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ email: '', password: '' });

  const mutation = useMutation({
    mutationFn: () => authenticate(form),
    onSuccess: (token) => {
      login(token.accessToken);
      navigate('/');
    }
  });

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    mutation.mutate();
  };

  return (
    <div className="container">
      <section>
        <SectionHeader
          title="로그인"
          description="/api/v1/login API를 통해 JWT 액세스 토큰을 발급받습니다"
        />
        <form onSubmit={handleSubmit} className="card" style={{ display: 'grid', gap: '1rem', maxWidth: '420px' }}>
          <label>
            <span>이메일</span>
            <input
              type="email"
              value={form.email}
              onChange={(event) => setForm((prev) => ({ ...prev, email: event.target.value }))}
              required
            />
          </label>
          <label>
            <span>비밀번호</span>
            <input
              type="password"
              value={form.password}
              onChange={(event) => setForm((prev) => ({ ...prev, password: event.target.value }))}
              required
            />
          </label>
          <button type="submit" className="primary" disabled={mutation.isPending}>
            로그인
          </button>
          {mutation.error ? <div className="alert">{(mutation.error as Error).message}</div> : null}
          <p style={{ color: '#6b7280', fontSize: '0.9rem' }}>
            관리자 통계(/api/v1/admin/...)를 호출하려면 ADMIN 권한 계정으로 로그인해야 합니다.
          </p>
        </form>
      </section>
    </div>
  );
}

export default LoginPage;
