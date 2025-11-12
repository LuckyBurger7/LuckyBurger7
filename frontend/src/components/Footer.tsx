function Footer() {
  return (
    <footer style={{ padding: '2rem 1.5rem', background: '#111827', color: 'white', marginTop: '4rem' }}>
      <div className="container" style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <strong>LuckyBurger7</strong>
          <p style={{ margin: '0.5rem 0 0', color: '#9ca3af' }}>
            고성능 버거 주문 시스템을 위한 풀스택 레퍼런스 프로젝트
          </p>
        </div>
        <div style={{ textAlign: 'right' }}>
          <div style={{ fontWeight: 600 }}>백엔드 API</div>
          <p style={{ margin: '0.5rem 0 0', color: '#9ca3af' }}>Spring Boot · Redis · Spring Security · JPA</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
