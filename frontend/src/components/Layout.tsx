import { PropsWithChildren } from 'react';
import Navbar from './Navbar';
import Footer from './Footer';

function Layout({ children }: PropsWithChildren) {
  return (
    <div>
      <Navbar />
      <main>{children}</main>
      <Footer />
    </div>
  );
}

export default Layout;
