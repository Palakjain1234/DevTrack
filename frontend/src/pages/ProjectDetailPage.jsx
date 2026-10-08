import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getProject, deleteProject, addMember, removeMember } from '../api/projects'
import { getReleases, createRelease } from '../api/releases'
import { getEnvironments, createEnvironment, deleteEnvironment } from '../api/environments'
import { searchUserByEmail } from '../api/users'
import StreamTable from '../components/ui/StreamTable'
import StatusBadge from '../components/ui/StatusBadge'
import Button from '../components/ui/Button'
import Modal from '../components/ui/Modal'

export default function ProjectDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [project, setProject] = useState(null)
  const [releases, setReleases] = useState([])
  const [environments, setEnvironments] = useState([])

  // Release modal
  const [isReleaseModal, setReleaseModal] = useState(false)
  const [version, setVersion] = useState('')
  const [title, setTitle] = useState('')
  const [releaseDesc, setReleaseDesc] = useState('')

  // Environment modal
  const [isEnvModal, setEnvModal] = useState(false)
  const [envName, setEnvName] = useState('')
  const [envType, setEnvType] = useState('DEV')
  const [envUrl, setEnvUrl] = useState('')

  // Member modal
  const [isMemberModal, setMemberModal] = useState(false)
  const [memberEmail, setMemberEmail] = useState('')
  const [memberSearch, setMemberSearch] = useState(null)
  const [memberError, setMemberError] = useState('')

  const load = () => {
    getProject(id).then(res => setProject(res.data))
    getReleases(id).then(res => setReleases(res.data))
    getEnvironments(id).then(res => setEnvironments(res.data))
  }
  useEffect(() => { load() }, [id])

  const handleCreateRelease = async (e) => {
    e.preventDefault()
    await createRelease(id, { version, title, description: releaseDesc })
    setReleaseModal(false)
    setVersion(''); setTitle(''); setReleaseDesc('')
    load()
  }

  const handleCreateEnv = async (e) => {
    e.preventDefault()
    await createEnvironment(id, { name: envName, type: envType, url: envUrl })
    setEnvModal(false)
    setEnvName(''); setEnvUrl(''); setEnvType('DEV')
    load()
  }

  const handleDeleteEnv = async (envId) => {
    if (!confirm('Delete this environment?')) return
    try {
      await deleteEnvironment(id, envId)
      load()
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to delete environment')
    }
  }

  const handleSearchMember = async () => {
    setMemberError('')
    setMemberSearch(null)
    try {
      const res = await searchUserByEmail(memberEmail)
      setMemberSearch(res.data)
    } catch {
      setMemberError('No user found with that email')
    }
  }

  const handleAddMember = async () => {
    await addMember(id, memberSearch.id)
    setMemberModal(false)
    setMemberEmail(''); setMemberSearch(null)
    load()
  }

  const handleRemoveMember = async (userId) => {
    if (!confirm('Remove this member?')) return
    try {
      await removeMember(id, userId)
      load()
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to remove member')
    }
  }

  const handleDelete = async () => {
    if (!confirm('Delete this project? This cannot be undone.')) return
    try {
      await deleteProject(id)
      navigate('/projects')
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to delete project')
    }
  }

  if (!project) return null

  return (
    <div className="flex gap-6">
      <div style={{ writingMode: 'vertical-rl', transform: 'rotate(180deg)', fontSize: '2rem', fontWeight: 700, letterSpacing: '-0.02em', borderRight: 'var(--border)', paddingRight: 'var(--space-4)' }}>
        {project.name}
      </div>

      <div className="flex flex-col gap-10" style={{ flex: 1 }}>

        {/* Header */}
        <div className="flex justify-between items-start">
          <p className="editorial text-muted">{project.description}</p>
          <Button variant="danger" onClick={handleDelete}>DELETE PROJECT</Button>
        </div>

        {/* Environments */}
        <div>
          <div className="flex justify-between items-end mb-4">
            <h2 className="label-caps">ENVIRONMENTS</h2>
            <Button variant="ghost" onClick={() => setEnvModal(true)}>+ ADD ENVIRONMENT</Button>
          </div>
          {environments.length === 0
            ? <p className="editorial text-muted">No environments configured.</p>
            : (
              <div className="flex flex-col gap-0" style={{ borderTop: 'var(--border)' }}>
                {environments.map(env => (
                  <div key={env.id} className="flex justify-between items-center" style={{ padding: 'var(--space-3) 0', borderBottom: 'var(--border-light)' }}>
                    <div className="flex gap-6 items-center">
                      <span className="label-caps">{env.type}</span>
                      <span className="editorial">{env.name}</span>
                      <span className="mono text-muted" style={{ fontSize: '12px' }}>{env.url}</span>
                    </div>
                    <div className="flex gap-3 items-center">
                      <StatusBadge status={env.status} />
                      <Button variant="danger" onClick={() => handleDeleteEnv(env.id)} style={{ padding: '2px 10px', fontSize: '11px' }}>✕</Button>
                    </div>
                  </div>
                ))}
              </div>
            )
          }
        </div>

        {/* Members */}
        <div>
          <div className="flex justify-between items-end mb-4">
            <h2 className="label-caps">MEMBERS ({project.memberIds?.length ?? 0})</h2>
            <Button variant="ghost" onClick={() => setMemberModal(true)}>+ ADD MEMBER</Button>
          </div>
          <div className="flex flex-wrap gap-2">
            {(project.memberIds ?? []).map(uid => (
              <div key={uid} className="flex items-center gap-2" style={{ padding: '4px 10px', border: 'var(--border)', fontSize: '13px' }}>
                <span className="mono">#{uid}</span>
                <button onClick={() => handleRemoveMember(uid)} style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--color-accent)', fontWeight: 700 }}>✕</button>
              </div>
            ))}
          </div>
        </div>

        {/* Releases */}
        <div>
          <div className="flex justify-between items-end mb-4">
            <h2 className="label-caps">RELEASES</h2>
            <Button variant="ghost" onClick={() => setReleaseModal(true)}>+ NEW RELEASE</Button>
          </div>
          <StreamTable
            columns={[
              { header: 'VERSION', accessor: 'version' },
              { header: 'TITLE', accessor: 'title' },
              { header: 'STATUS', render: row => <StatusBadge status={row.status} /> },
              { header: 'CREATED', render: row => new Date(row.createdAt).toLocaleDateString() },
            ]}
            data={releases}
            onRowClick={(row) => navigate(`/projects/${id}/releases/${row.id}`)}
          />
        </div>
      </div>

      {/* New Release Modal */}
      <Modal isOpen={isReleaseModal} onClose={() => setReleaseModal(false)} title="NEW RELEASE">
        <form onSubmit={handleCreateRelease} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="label-caps">Version</label>
            <input placeholder="e.g. v1.0.0" className="border-ink" style={{ padding: '8px', backgroundColor: 'transparent' }} value={version} onChange={e => setVersion(e.target.value)} required />
          </div>
          <div className="flex flex-col gap-1">
            <label className="label-caps">Title</label>
            <input placeholder="Release title" className="border-ink" style={{ padding: '8px', backgroundColor: 'transparent' }} value={title} onChange={e => setTitle(e.target.value)} required />
          </div>
          <div className="flex flex-col gap-1">
            <label className="label-caps">Description</label>
            <textarea placeholder="What's in this release?" className="border-ink" style={{ padding: '8px', backgroundColor: 'transparent', minHeight: '80px' }} value={releaseDesc} onChange={e => setReleaseDesc(e.target.value)} />
          </div>
          <Button type="submit">CREATE RELEASE</Button>
        </form>
      </Modal>

      {/* New Environment Modal */}
      <Modal isOpen={isEnvModal} onClose={() => setEnvModal(false)} title="ADD ENVIRONMENT">
        <form onSubmit={handleCreateEnv} className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="label-caps">Name</label>
            <input placeholder="e.g. Production EU" className="border-ink" style={{ padding: '8px', backgroundColor: 'transparent' }} value={envName} onChange={e => setEnvName(e.target.value)} required />
          </div>
          <div className="flex flex-col gap-1">
            <label className="label-caps">Type</label>
            <select value={envType} onChange={e => setEnvType(e.target.value)} style={{ padding: '8px', border: 'var(--border)', backgroundColor: 'transparent' }}>
              <option value="DEV">DEV</option>
              <option value="STAGING">STAGING</option>
              <option value="PRODUCTION">PRODUCTION</option>
            </select>
          </div>
          <div className="flex flex-col gap-1">
            <label className="label-caps">URL</label>
            <input placeholder="https://..." className="border-ink" style={{ padding: '8px', backgroundColor: 'transparent' }} value={envUrl} onChange={e => setEnvUrl(e.target.value)} required />
          </div>
          <Button type="submit">ADD ENVIRONMENT</Button>
        </form>
      </Modal>

      {/* Add Member Modal */}
      <Modal isOpen={isMemberModal} onClose={() => { setMemberModal(false); setMemberEmail(''); setMemberSearch(null); setMemberError('') }} title="ADD MEMBER">
        <div className="flex flex-col gap-4">
          <div className="flex flex-col gap-1">
            <label className="label-caps">Search by Email</label>
            <div className="flex gap-2">
              <input
                placeholder="user@example.com"
                className="border-ink"
                style={{ padding: '8px', backgroundColor: 'transparent', flex: 1 }}
                value={memberEmail}
                onChange={e => { setMemberEmail(e.target.value); setMemberSearch(null); setMemberError('') }}
                onKeyDown={e => e.key === 'Enter' && handleSearchMember()}
              />
              <Button variant="ghost" onClick={handleSearchMember}>SEARCH</Button>
            </div>
          </div>
          {memberError && <div className="editorial" style={{ color: 'var(--color-accent)' }}>{memberError}</div>}
          {memberSearch && (
            <div className="flex justify-between items-center" style={{ padding: 'var(--space-3)', border: 'var(--border)' }}>
              <div className="flex flex-col gap-1">
                <span style={{ fontWeight: 600 }}>{memberSearch.name}</span>
                <span className="mono text-muted" style={{ fontSize: '12px' }}>{memberSearch.email}</span>
              </div>
              <Button onClick={handleAddMember}>ADD</Button>
            </div>
          )}
        </div>
      </Modal>
    </div>
  )
}
