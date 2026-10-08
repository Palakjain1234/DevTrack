import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getRelease, updateReleaseStatus, deleteRelease } from '../api/releases'
import { triggerDeployment, getDeploysByRelease } from '../api/deployments'
import { getEnvironments } from '../api/environments'
import StatusBadge from '../components/ui/StatusBadge'
import StreamTable from '../components/ui/StreamTable'
import Button from '../components/ui/Button'
import Modal from '../components/ui/Modal'

const RELEASE_STATUSES = ['PLANNED', 'IN_PROGRESS', 'DEPLOYED', 'FAILED', 'ROLLED_BACK']

export default function ReleaseDetailPage() {
  const { projectId, releaseId } = useParams()
  const navigate = useNavigate()
  const [release, setRelease] = useState(null)
  const [deployments, setDeployments] = useState([])
  const [environments, setEnvironments] = useState([])

  const [isDeployModal, setDeployModal] = useState(false)
  const [selectedEnvId, setSelectedEnvId] = useState('')

  const [isStatusModal, setStatusModal] = useState(false)
  const [newStatus, setNewStatus] = useState('')

  const load = () => {
    getRelease(projectId, releaseId).then(res => setRelease(res.data))
    getDeploysByRelease(releaseId).then(res => setDeployments(res.data))
    getEnvironments(projectId).then(res => {
      setEnvironments(res.data)
      if (res.data.length > 0) setSelectedEnvId(res.data[0].id)
    })
  }
  useEffect(() => { load() }, [projectId, releaseId])

  const handleDeploy = async (e) => {
    e.preventDefault()
    await triggerDeployment({ releaseId: parseInt(releaseId), environmentId: parseInt(selectedEnvId) })
    setDeployModal(false)
    load()
  }

  const handleStatusUpdate = async (e) => {
    e.preventDefault()
    await updateReleaseStatus(projectId, releaseId, newStatus)
    setStatusModal(false)
    load()
  }

  const handleDelete = async () => {
    if (!confirm('Delete this release?')) return
    try {
      await deleteRelease(projectId, releaseId)
      navigate(`/projects/${projectId}`)
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to delete release')
    }
  }

  if (!release) return null

  const canDeploy = !['DEPLOYED', 'FAILED', 'ROLLED_BACK'].includes(release.status)

  return (
    <div className="flex flex-col gap-8">
      {/* Header */}
      <div className="flex justify-between items-end" style={{ borderBottom: 'var(--border)', paddingBottom: 'var(--space-4)' }}>
        <div>
          <div className="label-caps mb-2">RELEASE {release.version}</div>
          <h1 style={{ fontSize: '2.5rem', lineHeight: 1 }}>{release.title}</h1>
          {release.description && <p className="editorial text-muted" style={{ marginTop: '8px' }}>{release.description}</p>}
        </div>
        <div className="flex gap-3 items-center">
          <StatusBadge status={release.status} />
          <Button variant="ghost" onClick={() => { setNewStatus(release.status); setStatusModal(true) }}>UPDATE STATUS</Button>
          {canDeploy && environments.length > 0 && (
            <Button onClick={() => setDeployModal(true)}>TRIGGER DEPLOYMENT</Button>
          )}
          <Button variant="danger" onClick={handleDelete}>DELETE</Button>
        </div>
      </div>

      {/* Deployment history */}
      <div>
        <h2 className="label-caps mb-4">DEPLOYMENT HISTORY</h2>
        <StreamTable
          columns={[
            { header: 'ID', accessor: 'id' },
            { header: 'ENVIRONMENT', render: row => {
              const env = environments.find(e => e.id === row.environmentId)
              return env ? `${env.type} — ${env.name}` : `Env #${row.environmentId}`
            }},
            { header: 'STATUS', render: row => <StatusBadge status={row.status} /> },
            { header: 'STARTED', render: row => new Date(row.startedAt).toLocaleString() },
            { header: 'COMPLETED', render: row => row.completedAt ? new Date(row.completedAt).toLocaleString() : '—' },
          ]}
          data={deployments}
          onRowClick={row => navigate(`/deployments/${row.id}`)}
        />
        {deployments.length === 0 && <p className="editorial text-muted">No deployments yet.</p>}
      </div>

      {/* Trigger Deployment Modal */}
      <Modal isOpen={isDeployModal} onClose={() => setDeployModal(false)} title="TRIGGER DEPLOYMENT">
        <form onSubmit={handleDeploy} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="label-caps">Target Environment</label>
            {environments.length === 0
              ? <p className="editorial text-muted">No environments configured for this project. Add one first.</p>
              : (
                <select
                  value={selectedEnvId}
                  onChange={e => setSelectedEnvId(e.target.value)}
                  style={{ padding: '8px', border: 'var(--border)', backgroundColor: 'transparent' }}
                  required
                >
                  {environments.map(env => (
                    <option key={env.id} value={env.id}>{env.type} — {env.name}</option>
                  ))}
                </select>
              )
            }
          </div>
          {environments.length > 0 && <Button type="submit">EXECUTE</Button>}
        </form>
      </Modal>

      {/* Update Status Modal */}
      <Modal isOpen={isStatusModal} onClose={() => setStatusModal(false)} title="UPDATE RELEASE STATUS">
        <form onSubmit={handleStatusUpdate} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="label-caps">New Status</label>
            <select
              value={newStatus}
              onChange={e => setNewStatus(e.target.value)}
              style={{ padding: '8px', border: 'var(--border)', backgroundColor: 'transparent' }}
            >
              {RELEASE_STATUSES.map(s => (
                <option key={s} value={s}>{s}</option>
              ))}
            </select>
          </div>
          <Button type="submit">APPLY</Button>
        </form>
      </Modal>
    </div>
  )
}
