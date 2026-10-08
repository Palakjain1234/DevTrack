import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { getDeployment, updateDeployStatus } from '../api/deployments'
import StatusBadge from '../components/ui/StatusBadge'
import PulseConsole from '../components/ui/PulseConsole'
import Button from '../components/ui/Button'

export default function DeploymentDetailPage() {
  const { id } = useParams()
  const [deployment, setDeployment] = useState(null)

  const load = () => getDeployment(id).then(res => setDeployment(res.data))

  useEffect(() => {
    load()
  }, [id])

  useEffect(() => {
    if (!deployment || deployment.status !== 'RUNNING') return
    const interval = setInterval(load, 2000)
    return () => clearInterval(interval)
  }, [deployment?.status])

  const handleStatusUpdate = async (status) => {
    await updateDeployStatus(id, status)
    load()
  }

  if (!deployment) return null

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-center pb-4" style={{ borderBottom: 'var(--border)' }}>
        <div>
          <h1 className="label-caps" style={{ fontSize: '14px' }}>EXECUTION #{deployment.id}</h1>
          <div className="editorial text-muted mt-2">Release ID: {deployment.releaseId} | Env ID: {deployment.environmentId}</div>
        </div>
        <StatusBadge status={deployment.status} />
      </div>

      {deployment.status === 'RUNNING' && (
        <div className="flex gap-4">
          <Button variant="ghost" onClick={() => handleStatusUpdate('SUCCESS')}>SIMULATE SUCCESS</Button>
          <Button variant="danger" onClick={() => handleStatusUpdate('FAILED')}>SIMULATE FAILED</Button>
        </div>
      )}

      <PulseConsole logs={deployment.logs} />
    </div>
  )
}
