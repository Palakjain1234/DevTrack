import { createContext, useContext, useState, useCallback } from 'react'
import { login as apiLogin, register as apiRegister } from '../api/auth'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser]   = useState(() => {
    try { return JSON.parse(localStorage.getItem('dr_user')) } catch { return null }
  })
  const [token, setToken] = useState(() => localStorage.getItem('dr_token'))

  const doLogin = useCallback(async (email, password) => {
    const { data } = await apiLogin({ email, password })
    localStorage.setItem('dr_token', data.token)
    localStorage.setItem('dr_user', JSON.stringify(data.user))
    setToken(data.token)
    setUser(data.user)
    return data.user
  }, [])

  const doRegister = useCallback(async (name, email, password) => {
    const { data } = await apiRegister({ name, email, password })
    localStorage.setItem('dr_token', data.token)
    localStorage.setItem('dr_user', JSON.stringify(data.user))
    setToken(data.token)
    setUser(data.user)
    return data.user
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('dr_token')
    localStorage.removeItem('dr_user')
    setToken(null)
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider value={{ user, token, doLogin, doRegister, logout, isLoggedIn: !!token }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)
