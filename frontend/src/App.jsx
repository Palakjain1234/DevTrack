import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { NotifProvider } from './context/NotifContext'
import AppShell from './components/layout/AppShell'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import DashboardPage from './pages/DashboardPage'
import ProjectsPage from './pages/ProjectsPage'
import ProjectDetailPage from './pages/ProjectDetailPage'
import ReleasesPage from './pages/ReleasesPage'
import ReleaseDetailPage from './pages/ReleaseDetailPage'
import DeploymentsPage from './pages/DeploymentsPage'
import DeploymentDetailPage from './pages/DeploymentDetailPage'
import NotificationsPage from './pages/NotificationsPage'

export default function App() {
  return (
    <AuthProvider>
      <NotifProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/" element={<AppShell />}>
              <Route index element={<DashboardPage />} />
              <Route path="projects" element={<ProjectsPage />} />
              <Route path="projects/:id" element={<ProjectDetailPage />} />
              <Route path="projects/:projectId/releases" element={<ReleasesPage />} />
              <Route path="projects/:projectId/releases/:releaseId" element={<ReleaseDetailPage />} />
              <Route path="deployments" element={<DeploymentsPage />} />
              <Route path="deployments/:id" element={<DeploymentDetailPage />} />
              <Route path="notifications" element={<NotificationsPage />} />
            </Route>
          </Routes>
        </BrowserRouter>
      </NotifProvider>
    </AuthProvider>
  )
}
