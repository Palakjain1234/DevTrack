export default function Modal({ isOpen, onClose, title, children }) {
  if (!isOpen) return null

  return (
    <div style={{ position: 'fixed', inset: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 }}>
      <div style={{ backgroundColor: 'var(--color-paper)', border: 'var(--border)', width: '100%', maxWidth: '500px', boxShadow: 'var(--shadow-md)' }}>
        <div className="flex justify-between items-center" style={{ padding: 'var(--space-3) var(--space-4)', borderBottom: 'var(--border)' }}>
          <h2 className="label-caps">{title}</h2>
          <button onClick={onClose} style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: '18px' }}>&times;</button>
        </div>
        <div style={{ padding: 'var(--space-4)' }}>
          {children}
        </div>
      </div>
    </div>
  )
}
