import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getProjects, createProject } from '../api/projects'
import StreamTable from '../components/ui/StreamTable'
import Button from '../components/ui/Button'
import Modal from '../components/ui/Modal'

export default function ProjectsPage() {
  const [projects, setProjects] = useState([])
  const [isModalOpen, setModalOpen] = useState(false)
  const [name, setName] = useState('')
  const [desc, setDesc] = useState('')
  const navigate = useNavigate()

  const load = () => getProjects().then(res => setProjects(res.data))
  useEffect(() => { load() }, [])

  const handleCreate = async (e) => {
    e.preventDefault()
    await createProject({ name, description: desc })
    setModalOpen(false)
    load()
  }

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-end">
        <div>
          <h1 style={{ fontSize: '4rem', lineHeight: 1, letterSpacing: '-0.02em' }}>SOURCE VAULT</h1>
          <p className="editorial text-muted">All registered projects under your clearance</p>
        </div>
        <Button variant="ghost" onClick={() => setModalOpen(true)}>+ NEW PROJECT</Button>
      </div>

      <StreamTable
        columns={[
          { header: 'ID', accessor: 'id' },
          { header: 'NAME', accessor: 'name' },
          { header: 'DESCRIPTION', render: row => <span className="editorial">{row.description}</span> },
          { header: 'MEMBERS', render: row => row.memberIds.length },
          { header: 'CREATED', render: row => new Date(row.createdAt).toLocaleDateString() },
        ]}
        data={projects}
        onRowClick={(row) => navigate(`/projects/${row.id}`)}
      />

      <Modal isOpen={isModalOpen} onClose={() => setModalOpen(false)} title="REGISTER PROJECT">
        <form onSubmit={handleCreate} className="flex flex-col gap-4">
          <input placeholder="Project Name" className="border-ink" style={{ padding: '8px' }} value={name} onChange={e=>setName(e.target.value)} required />
          <textarea placeholder="Description" className="border-ink" style={{ padding: '8px' }} value={desc} onChange={e=>setDesc(e.target.value)} />
          <Button type="submit">CREATE</Button>
        </form>
      </Modal>
    </div>
  )
}
