import api from './axios'
export const getReleases    = (projectId)       => api.get(`/projects/${projectId}/releases`)
export const getRelease     = (projectId, id)   => api.get(`/projects/${projectId}/releases/${id}`)
export const createRelease  = (projectId, data) => api.post(`/projects/${projectId}/releases`, data)
export const updateReleaseStatus = (projectId, id, status) =>
  api.patch(`/projects/${projectId}/releases/${id}/status`, { status })
export const deleteRelease  = (projectId, id)   => api.delete(`/projects/${projectId}/releases/${id}`)
