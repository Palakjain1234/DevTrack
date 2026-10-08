import { Outlet, Navigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import Sidebar from './Sidebar'
import TopBar from './TopBar'

export default function AppShell() {
  const { isLoggedIn } = useAuth()

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />
  }

  return (
    <div className="flex" style={{ minHeight: '100vh' }}>
      <Sidebar />
      <div className="flex flex-col" style={{ flex: 1, borderLeft: 'var(--border)' }}>
        <TopBar />
        <main style={{ padding: 'var(--space-6)', flex: 1, overflowY: 'auto' }}>
          <Outlet />
        </main>
      </div>
    </div>
  )
}
