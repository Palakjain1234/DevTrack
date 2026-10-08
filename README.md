# DevRelease

A release pipeline tracker for software teams — manage projects, environments, releases, and deployments end-to-end from a single dashboard.

<br/>

## Features

- **Projects** — create projects, invite team members, manage access
- **Environments** — configure DEV, STAGING, and PRODUCTION targets per project
- **Releases** — version and track releases through their full lifecycle (`PLANNED → IN_PROGRESS → DEPLOYED → ROLLED_BACK`)
- **Deployments** — trigger deployments to specific environments, track status and logs in real time
- **Notifications** — automatic in-app alerts for every release and deployment event
- **Dashboard** — live stats: total projects, releases, deployments today, and success rate
- **JWT Auth** — stateless authentication with role-based access (ADMIN / DEVELOPER)
- **Swagger UI** — full API documentation at `/swagger-ui.html`

<br/>

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 3.2, Spring Security, JPA/Hibernate |
| Frontend | React 18, Vite, React Router, Axios |
| Database | MySQL 8 |
| Auth | JWT (JJWT 0.11) |
| API Docs | SpringDoc OpenAPI / Swagger UI |
| Infra | Docker, Docker Compose, Nginx |

<br/>

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose

### Run locally

```bash
git clone https://github.com/YOUR_USERNAME/devrelease.git
cd devrelease
docker-compose up --build -d
```

Open **http://localhost** in your browser.

A default admin account is seeded automatically:

```
Email:    admin@devrelease.io
Password: Admin@123
```

<br/>

## Usage Flow

```
Create Project → Add Environments → Create Release → Trigger Deployment
```

1. **Create a project** from the Source Vault page
2. **Add environments** (DEV / STAGING / PRODUCTION) inside the project
3. **Create a release** with a version number and title
4. **Trigger a deployment** — pick a target environment from the release page
5. **Update deployment status** (Success / Failed) from the deployment detail page
6. **Monitor** everything from the Dashboard

<br/>

## API Reference

Full interactive API docs available at:

```
http://localhost:8080/swagger-ui.html
```

Key endpoints:

```
POST   /api/auth/register
POST   /api/auth/login

GET    /api/projects
POST   /api/projects
POST   /api/projects/{id}/members/{userId}

GET    /api/projects/{projectId}/environments
POST   /api/projects/{projectId}/environments

GET    /api/projects/{projectId}/releases
POST   /api/projects/{projectId}/releases
PATCH  /api/projects/{projectId}/releases/{id}/status

POST   /api/deployments
PATCH  /api/deployments/{id}/status

GET    /api/dashboard/stats
GET    /api/notifications
```

<br/>

## Project Structure

```
devrelease/
├── backend/                  # Spring Boot API
│   ├── src/main/java/com/devrelease/
│   │   ├── controller/       # REST controllers
│   │   ├── service/          # Business logic
│   │   ├── model/            # JPA entities
│   │   ├── repository/       # Spring Data repositories
│   │   ├── dto/              # Request / Response DTOs
│   │   ├── security/         # JWT filter & utilities
│   │   └── enums/            # Status enums
│   └── Dockerfile
├── frontend/                 # React + Vite SPA
│   ├── src/
│   │   ├── pages/            # Route-level page components
│   │   ├── components/       # Shared UI components
│   │   ├── api/              # Axios API modules
│   │   └── context/          # Auth & notification context
│   ├── nginx.conf
│   └── Dockerfile
└── docker-compose.yml
```

<br/>

## License

MIT
