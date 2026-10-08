export default function PulseConsole({ logs }) {
  return (
    <div style={{ border: 'var(--border)', backgroundColor: '#0D0D0D', color: '#FFF', padding: 'var(--space-4)', borderRadius: 'var(--radius-sm)' }}>
      <div className="label-caps" style={{ color: 'var(--color-muted)', marginBottom: 'var(--space-3)' }}>EXECUTION LOG</div>
      <pre className="mono" style={{ whiteSpace: 'pre-wrap', wordBreak: 'break-all', fontSize: '12px' }}>
        {logs || 'No logs available.'}
      </pre>
      <div style={{ display: 'flex', alignItems: 'center', marginTop: 'var(--space-2)' }}>
        <span style={{ color: 'var(--color-accent)' }}>&gt;</span>
        <span className="pulse-dot" style={{ marginLeft: 'var(--space-1)' }} />
      </div>
    </div>
  )
}
