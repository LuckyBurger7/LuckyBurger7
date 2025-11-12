import { Link } from 'react-router-dom';

function NotFoundPage() {
  return (
    <div className="container" style={{ textAlign: 'center', padding: '4rem 0' }}>
      <div className="card" style={{ display: 'inline-block', padding: '3rem 4rem' }}>
        <h1 style={{ fontSize: '3rem', margin: 0, color: '#f97316' }}>404</h1>
        <p style={{ color: '#6b7280' }}>요청하신 페이지를 찾을 수 없습니다.</p>
        <Link to="/" style={{ color: '#f97316', fontWeight: 600 }}>
          홈으로 돌아가기 →
        </Link>
      </div>
    </div>
  );
}

export default NotFoundPage;
