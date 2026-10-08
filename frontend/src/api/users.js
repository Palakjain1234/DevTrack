import api from './axios'
export const searchUserByEmail = (email) => api.get(`/users/search?email=${encodeURIComponent(email)}`)
export const getMe             = ()      => api.get('/users/me')
