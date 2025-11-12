function ErrorState({ message, retry }: { message: string; retry?: () => void }) {
  return (
    <div className="card" style={{ background: '#fef2f2', borderColor: '#fecaca' }}>
      <h3 style={{ color: '#b91c1c' }}>문제가 발생했습니다</h3>
      <p style={{ color: '#7f1d1d' }}>{message}</p>
      {retry ? (
        <button type="button" className="primary" onClick={retry}>
          다시 시도
        </button>
      ) : null}
    </div>
  );
}

export default ErrorState;
