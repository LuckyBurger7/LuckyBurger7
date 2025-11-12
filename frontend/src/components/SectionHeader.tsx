import { ReactNode } from 'react';

function SectionHeader({ title, description, action }: { title: string; description?: string; action?: ReactNode }) {
  return (
    <div className="section-header">
      <div>
        <h2>{title}</h2>
        {description ? <p style={{ color: '#6b7280', marginTop: '0.5rem' }}>{description}</p> : null}
      </div>
      {action}
    </div>
  );
}

export default SectionHeader;
