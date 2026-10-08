export default function EmptyState({ message }) {
  return (
    <div className="flex flex-col items-center justify-center" style={{ padding: 'var(--space-12) var(--space-6)', border: 'var(--border-light)', backgroundColor: 'var(--color-surface)' }}>
      <p className="editorial text-muted">{message}</p>
    </div>
  )
}
