import { useAuth } from '../../context/AuthContext'
import { useNotif } from '../../context/NotifContext'
import { LogOut, Bell } from 'lucide-react'
import { useNavigate } from 'react-router-dom'

export default function TopBar() {
  const { user, logout } = useAuth()
  const { unreadCount } = useNotif()
  const navigate = useNavigate()

  return (
    <header className="flex justify-between items-center border-ink" style={{ padding: 'var(--space-3) var(--space-6)', borderBottom: 'var(--border)' }}>
      <div className="editorial" style={{ fontSize: '18px' }}>DevRelease Tracker</div>
      <div className="flex items-center gap-6">
        <div style={{ position: 'relative', cursor: 'pointer' }} onClick={() => navigate('/notifications')}>
          <Bell size={20} className="text-ink" />
          {unreadCount > 0 && (
            <span className="unread-dot" style={{ position: 'absolute', top: -2, right: -2 }} />
          )}
        </div>
        <div className="flex items-center gap-2">
          <span className="mono">{user?.email}</span>
          <span className="status-badge status-running">{user?.role}</span>
        </div>
        <button onClick={logout} style={{ background: 'none', border: 'none', cursor: 'pointer' }}><LogOut size={18} className="text-muted" /></button>
      </div>
    </header>
  )
}
