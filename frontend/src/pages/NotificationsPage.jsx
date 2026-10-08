import { useEffect, useState } from 'react'
import { getNotifications, markRead, markAllRead } from '../api/notifications'
import Button from '../components/ui/Button'

export default function NotificationsPage() {
  const [notifications, setNotifications] = useState([])

  const load = () => getNotifications().then(res => setNotifications(res.data))
  useEffect(() => { load() }, [])

  const handleMarkAll = async () => {
    await markAllRead()
    load()
  }

  const handleMarkRead = async (id) => {
    await markRead(id)
    load()
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-end">
        <h1 style={{ fontSize: '3rem', letterSpacing: '-0.02em' }}>SIGNAL FEED</h1>
        <Button variant="ghost" onClick={handleMarkAll}>MARK ALL READ</Button>
      </div>
      <div className="flex flex-col gap-0 border-ink" style={{ borderTop: 'var(--border)' }}>
        {notifications.map(n => (
          <div key={n.id} className="flex justify-between items-center" style={{ padding: 'var(--space-4)', borderBottom: 'var(--border-light)', borderLeft: n.read ? 'none' : '3px solid var(--color-accent)', backgroundColor: n.read ? 'transparent' : 'var(--color-surface)' }}>
            <div className="flex flex-col gap-1">
              <span className="label-caps" style={{ color: n.read ? 'var(--color-muted)' : 'var(--color-ink)' }}>{n.type.replace(/_/g, ' ')}</span>
              <span className="editorial">{n.message}</span>
              <span className="mono text-muted">{new Date(n.createdAt).toLocaleString()}</span>
            </div>
            {!n.read && <Button variant="ghost" onClick={() => handleMarkRead(n.id)}>Acknowledge</Button>}
          </div>
        ))}
        {notifications.length === 0 && <div className="editorial text-muted text-center" style={{ padding: 'var(--space-8)' }}>No signals detected.</div>}
      </div>
    </div>
  )
}
