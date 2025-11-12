import { ReactNode } from 'react';

interface DataCardProps {
  title: string;
  value: ReactNode;
  subtitle?: string;
  accent?: ReactNode;
}

function DataCard({ title, value, subtitle, accent }: DataCardProps) {
  return (
    <div className="card">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <div>
          <div style={{ color: '#6b7280', fontWeight: 600, textTransform: 'uppercase', fontSize: '0.8rem' }}>{title}</div>
          <div style={{ fontSize: '2rem', fontWeight: 700, marginTop: '0.5rem' }}>{value}</div>
          {subtitle ? <div style={{ marginTop: '0.5rem', color: '#6b7280' }}>{subtitle}</div> : null}
        </div>
        {accent ? <div>{accent}</div> : null}
      </div>
    </div>
  );
}

export default DataCard;
