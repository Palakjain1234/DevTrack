import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getDeployments } from '../api/deployments'
import { getProjects } from '../api/projects'
import { getReleases } from '../api/releases'
import { getEnvironments } from '../api/environments'
import StreamTable from '../components/ui/StreamTable'
import StatusBadge from '../components/ui/StatusBadge'

export default function DeploymentsPage() {
  const [deployments, setDeployments] = useState([])
  const [releaseMap, setReleaseMap] = useState({})
  const [envMap, setEnvMap] = useState({})
  const navigate = useNavigate()

  useEffect(() => {
    const fetchAll = async () => {
      const [deploysRes, projectsRes] = await Promise.all([getDeployments(), getProjects()])
      const deploys = deploysRes.data
      setDeployments(deploys)

      // Build release and environment maps from all projects
      const projects = projectsRes.data
      const relMap = {}
      const eMap = {}

      await Promise.all(projects.map(async (p) => {
        const [relRes, envRes] = await Promise.all([getReleases(p.id), getEnvironments(p.id)])
        relRes.data.forEach(r => { relMap[r.id] = r.version })
        envRes.data.forEach(e => { eMap[e.id] = `${e.type} — ${e.name}` })
      }))

      setReleaseMap(relMap)
      setEnvMap(eMap)
    }
    fetchAll()
  }, [])

  return (
    <div className="flex flex-col gap-6">
      <h1 style={{ fontSize: '3rem', letterSpacing: '-0.02em' }}>EXECUTION REGISTRY</h1>
      <StreamTable
        columns={[
          { header: 'ID', accessor: 'id' },
          { header: 'RELEASE', render: row => releaseMap[row.releaseId] ?? `#${row.releaseId}` },
          { header: 'ENVIRONMENT', render: row => envMap[row.environmentId] ?? `#${row.environmentId}` },
          { header: 'STATUS', render: row => <StatusBadge status={row.status} /> },
          { header: 'STARTED', render: row => new Date(row.startedAt).toLocaleString() },
          { header: 'COMPLETED', render: row => row.completedAt ? new Date(row.completedAt).toLocaleString() : '—' },
        ]}
        data={deployments}
        onRowClick={row => navigate(`/deployments/${row.id}`)}
      />
      {deployments.length === 0 && <p className="editorial text-muted">No deployments yet.</p>}
    </div>
  )
}
