import { useEffect, useState } from 'react'
import { getDashboardStats } from '../api/dashboard'
import { getDeployments } from '../api/deployments'
import StatCard from '../components/ui/StatCard'
import StreamTable from '../components/ui/StreamTable'
import StatusBadge from '../components/ui/StatusBadge'

export default function DashboardPage() {
  const [stats, setStats] = useState(null)
  const [recentDeployments, setRecentDeployments] = useState([])

  useEffect(() => {
    getDashboardStats().then(res => setStats(res.data))
    getDeployments().then(res => setRecentDeployments(res.data.slice(0, 10)))
  }, [])

  if (!stats) return <div className="editorial">Loading telemetry...</div>

  return (
    <div className="flex flex-col gap-8">
      <div className="flex gap-6">
        <StatCard title="TOTAL PROJECTS" value={stats.totalProjects} />
        <StatCard title="TOTAL RELEASES" value={stats.totalReleases} />
        <StatCard title="DEPLOYMENTS TODAY" value={stats.deploymentsToday} />
        <StatCard title="SUCCESS RATE %" value={stats.successRate.toFixed(1)} highlight={stats.successRate < 80} />
      </div>
      <div>
        <h2 className="label-caps" style={{ marginBottom: 'var(--space-4)' }}>RECENT DEPLOYMENTS</h2>
        <StreamTable
          columns={[
            { header: 'ID', accessor: 'id' },
            { header: 'STATUS', render: (row) => <StatusBadge status={row.status} /> },
            { header: 'STARTED', render: (row) => new Date(row.startedAt).toLocaleString() },
          ]}
          data={recentDeployments}
        />
      </div>
    </div>
  )
}
