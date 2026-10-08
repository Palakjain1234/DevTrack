export default function StatusBadge({ status }) {
  let colorClass = 'status-pending'
  if (['SUCCESS', 'DEPLOYED'].includes(status)) colorClass = 'status-success'
  if (['FAILED', 'ROLLED_BACK'].includes(status)) colorClass = 'status-failed'
  if (['RUNNING', 'IN_PROGRESS'].includes(status)) colorClass = 'status-running'

  return (
    <span className={`status-badge ${colorClass}`}>
      {['RUNNING', 'IN_PROGRESS'].includes(status) && <span className="pulse-dot" />}
      {status}
    </span>
  )
}
