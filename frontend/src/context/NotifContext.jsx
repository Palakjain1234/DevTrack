import { createContext, useContext, useState, useCallback, useEffect, useRef } from 'react'
import { getUnreadCount } from '../api/notifications'
import { useAuth } from './AuthContext'

const NotifContext = createContext(null)

export function NotifProvider({ children }) {
  const { isLoggedIn } = useAuth()
  const [unreadCount, setUnreadCount] = useState(0)
  const intervalRef = useRef(null)

  const refreshUnreadCount = useCallback(async () => {
    try {
      const { data } = await getUnreadCount()
      setUnreadCount(data.count)
    } catch (error) {
      console.error('Failed to fetch unread count', error)
    }
  }, [])

  useEffect(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current)
      intervalRef.current = null
    }
    if (!isLoggedIn) {
      setUnreadCount(0)
      return
    }
    refreshUnreadCount()
    intervalRef.current = setInterval(refreshUnreadCount, 30000)
    return () => {
      clearInterval(intervalRef.current)
      intervalRef.current = null
    }
  }, [isLoggedIn, refreshUnreadCount])

  return (
    <NotifContext.Provider value={{ unreadCount, refreshUnreadCount }}>
      {children}
    </NotifContext.Provider>
  )
}

export const useNotif = () => useContext(NotifContext)
