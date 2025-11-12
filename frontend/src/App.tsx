import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout';
import HomePage from './pages/Home';
import MenusPage from './pages/Menus';
import ShopsPage from './pages/Shops';
import CouponsPage from './pages/Coupons';
import CartPage from './pages/Cart';
import LoginPage from './pages/Login';
import AdminDashboardPage from './pages/AdminDashboard';
import NotFoundPage from './pages/NotFound';

function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/menus" element={<MenusPage />} />
        <Route path="/shops" element={<ShopsPage />} />
        <Route path="/coupons" element={<CouponsPage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/admin" element={<AdminDashboardPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </Layout>
  );
}

export default App;
