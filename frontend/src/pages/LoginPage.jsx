import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import Button from '../components/ui/Button'

export default function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const { doLogin } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      await doLogin(email, password)
      navigate('/')
    } catch (err) {
      setError('Invalid credentials')
    }
  }

  return (
    <div className="flex" style={{ height: '100vh', backgroundColor: 'var(--color-paper)' }}>
      <div className="flex flex-col justify-center border-ink" style={{ flex: 1, padding: 'var(--space-16)', borderRight: 'var(--border)' }}>
        <h1 style={{ fontSize: '8rem', lineHeight: 0.9, letterSpacing: '-0.05em' }}>DEV<br/>RELEASE</h1>
      </div>
      <div className="flex items-center justify-center" style={{ flex: 1 }}>
        <form onSubmit={handleSubmit} className="flex flex-col gap-6" style={{ width: '400px', marginLeft: '10%' }}>
          <h2 className="editorial" style={{ fontSize: '24px' }}>Sign in to continue</h2>
          {error && <div className="text-accent editorial">{error}</div>}
          <div className="flex flex-col gap-2">
            <label className="label-caps">Email</label>
            <input type="email" value={email} onChange={e=>setEmail(e.target.value)} required style={{ border: 'none', borderBottom: 'var(--border)', padding: 'var(--space-2) 0', backgroundColor: 'transparent', outline: 'none' }} />
          </div>
          <div className="flex flex-col gap-2">
            <label className="label-caps">Password</label>
            <input type="password" value={password} onChange={e=>setPassword(e.target.value)} required style={{ border: 'none', borderBottom: 'var(--border)', padding: 'var(--space-2) 0', backgroundColor: 'transparent', outline: 'none' }} />
          </div>
          <Button type="submit" style={{ width: '100%', marginTop: 'var(--space-4)' }}>Sign In</Button>
          <div className="editorial text-muted" style={{ textAlign: 'center' }}>
            New agent? <Link to="/register" className="text-ink" style={{ textDecoration: 'underline' }}>Register</Link>
          </div>
        </form>
      </div>
    </div>
  )
}
