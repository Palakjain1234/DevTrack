export default function StatCard({ title, value, highlight }) {
  return (
    <div className="flex flex-col gap-2" style={{ borderBottom: 'var(--border)', paddingBottom: 'var(--space-4)', flex: 1 }}>
      <div className="label-caps">{title}</div>
      <div className="mono" style={{ fontSize: '36px', lineHeight: 1, color: highlight ? 'var(--color-accent)' : 'var(--color-ink)' }}>
        {value}
      </div>
    </div>
  )
}
