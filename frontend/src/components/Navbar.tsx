import { Link, NavLink, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const navItems = [
  { to: '/menus', label: '메뉴' },
  { to: '/shops', label: '매장' },
  { to: '/coupons', label: '쿠폰' },
  { to: '/cart', label: '장바구니' },
  { to: '/admin', label: '관리자 대시보드' }
];

function Navbar() {
  const { isAuthenticated, logout } = useAuth();
  const location = useLocation();

  return (
    <header
      style={{
        background: 'white',
        borderBottom: '1px solid #e5e7eb',
        position: 'sticky',
        top: 0,
        zIndex: 10
      }}
    >
      <div
        className="container"
        style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '1rem 1.5rem' }}
      >
        <Link to="/" style={{ fontWeight: 800, fontSize: '1.25rem', color: '#f97316' }}>
          LuckyBurger7
        </Link>
        <nav style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                [
                  'tab-button',
                  isActive || location.pathname.startsWith(item.to) ? 'active' : undefined
                ]
                  .filter(Boolean)
                  .join(' ')
              }
            >
              {item.label}
            </NavLink>
          ))}
          {isAuthenticated ? (
            <button className="tab-button" onClick={logout} type="button">
              로그아웃
            </button>
          ) : (
            <NavLink to="/login" className={({ isActive }) => ['tab-button', isActive ? 'active' : undefined].join(' ')}>
              로그인
            </NavLink>
          )}
        </nav>
      </div>
    </header>
  );
}

export default Navbar;
