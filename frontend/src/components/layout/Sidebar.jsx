import { NavLink } from 'react-router-dom'
import { LayoutDashboard, Folder, Send, Activity, Bell } from 'lucide-react'

export default function Sidebar() {
  const navItems = [
    { to: '/', icon: <LayoutDashboard size={20} />, label: 'DASHBOARD' },
    { to: '/projects', icon: <Folder size={20} />, label: 'PROJECTS' },
    { to: '/deployments', icon: <Activity size={20} />, label: 'DEPLOYMENTS' },
    { to: '/notifications', icon: <Bell size={20} />, label: 'NOTIFICATIONS' },
  ]

  return (
    <aside style={{ width: '64px', backgroundColor: 'var(--color-paper)', display: 'flex', flexDirection: 'column', alignItems: 'center', padding: 'var(--space-4) 0' }}>
      <div style={{ fontWeight: 900, fontSize: '24px', letterSpacing: '-2px', marginBottom: 'var(--space-8)' }}>DR</div>
      <nav className="flex flex-col gap-6 items-center">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) => `flex flex-col items-center gap-2 ${isActive ? 'text-accent' : 'text-muted'}`}
            style={{ textDecoration: 'none' }}
          >
            {item.icon}
            <span className="label-caps" style={{ writingMode: 'vertical-rl', transform: 'rotate(180deg)' }}>{item.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
