# DevTrack — Release & Deployment Management Platform

DevTrack is a full-stack web application built to simplify how software teams manage their release and deployment workflows. It provides a centralized platform where teams can track projects, configure environments, manage releases, and monitor deployments — all from a single dashboard.

---

## 🚀 Features

### Project Management
- Create and manage software projects
- Add and remove project members
- View all releases and deployments scoped to each project

### Release Management
- Create versioned releases with title, description, and status
- Set optional scheduled release dates
- Track release lifecycle from `PLANNED` → `IN_PROGRESS` → `DEPLOYED` → `ROLLED_BACK`
- Roll back deployed releases when needed

### Environment Management
- Configure multiple deployment targets per project
- Supports `DEV`, `STAGING`, and `PRODUCTION` environments
- Track which deployments ran on each environment

### Deployment Management
- Trigger deployments by targeting a release and an environment
- Monitor deployment status in real time (`RUNNING` → `SUCCESS` / `FAILED`)
- View deployment logs and history per release
- Simulate deployment outcomes for testing

### Dashboard & Activity
- Role-scoped stats: total projects, releases, deployments today, success rate
- Per-project activity feed combining recent releases and deployments into a timeline
- Global deployments view across all projects

### Authentication & Notifications
- JWT-based stateless authentication
- Role-based access control (`ADMIN` / `DEVELOPER`)
- Automatic in-app notifications for deployment triggers, successes, failures, and rollbacks

---

## 🔄 Application Flow

```
           PROJECT
              │
              ▼
  CONFIGURE ENVIRONMENTS
              │
              ▼
       CREATE RELEASE
              │
              ▼
     TRIGGER DEPLOYMENT
              │
              ▼
     TRACK DEPLOYMENT STATUS
              │
              ▼
   DASHBOARD & ACTIVITY FEED
```

> A project can have multiple releases. Each release can be deployed to multiple environments independently.

---

## 🏗️ Architecture

```
              ┌──────────────────────┐
              │    React Frontend     │
              │    Vite + Axios       │
              └──────────┬───────────┘
                         │
                      REST API
                         │
                         ▼
              ┌──────────────────────┐
              │   Spring Boot API    │
              │   Business Logic     │
              └──────────┬───────────┘
                         │
           ┌─────────────┴─────────────┐
           │                           │
           ▼                           ▼
   Spring Security              JPA / Hibernate
           │                           │
           ▼                           ▼
          JWT                      MySQL 8
```

---

## 🛠️ Technology Stack

**Frontend**
- React 18
- Vite
- React Router v6
- Axios

**Backend**
- Java 21
- Spring Boot 3.2
- Spring Security
- Spring Data JPA / Hibernate
- REST APIs

**Database**
- MySQL 8

**API Documentation**
- Swagger / OpenAPI (SpringDoc)

**Infrastructure**
- Docker
- Docker Compose
- Nginx

---

## 📂 Project Structure

```
DevTrack/
│
├── backend/
│   ├── src/main/java/com/devrelease/
│   │   ├── controller/        # REST controllers
│   │   ├── service/           # Business logic
│   │   ├── repository/        # Spring Data repositories
│   │   ├── model/             # JPA entities
│   │   ├── dto/               # Request & response DTOs
│   │   ├── security/          # JWT filter & utilities
│   │   └── enums/             # Status and type enums
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   │   ├── pages/             # Route-level page components
│   │   ├── components/        # Shared UI components
│   │   ├── api/               # Axios API modules
│   │   └── context/           # Auth & notification context
│   ├── nginx.conf
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md
```

---

## 🔐 Security

DevTrack uses Spring Security with JWT-based authentication. All API endpoints except `/api/auth/**` require a valid bearer token.

Users are assigned one of two roles at registration:

| Role | Access |
|------|--------|
| `ADMIN` | Global access — sees all projects, releases, deployments, and stats |
| `DEVELOPER` | Scoped access — sees only projects they own or are a member of |

---

## 📡 API Documentation

The backend exposes RESTful APIs for all core domains. Explore and test them via Swagger UI:

```
http://localhost:8081/swagger-ui.html
```

Available API groups:

- `POST /api/auth/register` — Register a new account
- `POST /api/auth/login` — Login and receive a JWT
- `/api/projects` — Project CRUD + member management + activity feed
- `/api/projects/{id}/environments` — Environment management
- `/api/projects/{id}/releases` — Release management + status updates
- `/api/deployments` — Trigger and track deployments
- `/api/notifications` — In-app notification inbox
- `/api/dashboard/stats` — Role-scoped dashboard statistics

---

## 🐳 Running with Docker

**Prerequisites:** [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running.

**Clone the repository**
```bash
git clone https://github.com/Palakjain1234/DevTrack.git
cd DevTrack
```

**Build and start all services**
```bash
docker-compose up --build
```

Once running, open **http://localhost** in your browser.

A default admin account is created automatically on first run:
```
Email:    admin@devrelease.io
Password: Admin@123
```

**Stop the application**
```bash
docker-compose down
```

---

## 🎯 Project Objective

DevTrack was built to make software release management more organized and transparent. Instead of tracking releases across Slack threads, spreadsheets, or emails, teams get a single platform that shows exactly what is being released, where it is being deployed, and the current status of every deployment.

The platform brings together project management, environment configuration, release tracking, deployment monitoring, role-based access, notifications, and activity tracking into one cohesive full-stack application.
