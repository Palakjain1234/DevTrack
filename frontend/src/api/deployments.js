import api from './axios'
export const getDeployments       = ()       => api.get('/deployments')
export const getDeployment        = (id)     => api.get(`/deployments/${id}`)
export const triggerDeployment    = (data)   => api.post('/deployments', data)
export const updateDeployStatus   = (id, status) => api.patch(`/deployments/${id}/status`, { status })
export const getDeploysByRelease  = (releaseId) => api.get(`/deployments/by-release/${releaseId}`)
export const getDeploysByEnv      = (envId)     => api.get(`/deployments/by-environment/${envId}`)
