# ShiftPilot

A full-stack shift management and employee scheduling system that enables managers to create and manage shifts, assign employees, handle shift swaps, process leave requests, and track approvals through dedicated dashboards.

---

## Features

- **Shift Management** — create, update, publish, and delete shifts with date/time and capacity settings
- **Employee Scheduling** — assign employees to shifts, view weekly schedules, and manage rosters
- **Shift Drafts** — save reusable shift templates for faster scheduling
- **Shift Swap Requests** — employees can request swaps with each other, subject to manager approval
- **Leave Requests** — employees submit leave requests that go through a manager approval workflow
- **Manager Approvals** — centralised approval queue for swap and leave requests
- **Role-Based Access Control** — three roles: Employee, Manager, and Admin with fine-grained permissions
- **Notifications** — in-app notifications for request status changes and assignments
- **Dashboards** — dedicated views for managers (team overview, pending approvals) and employees (personal schedule, requests)
- **JWT Authentication** — stateless auth with Bearer tokens stored in HTTP-only cookies

---

## Tech Stack

### Backend
| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Security + OAuth2 Resource Server | — |
| Spring Data JPA / Hibernate | — |
| PostgreSQL | — |
| JJWT | 0.11.5 |
| SpringDoc OpenAPI (Swagger) | 3.0.2 |
| Logbook (HTTP logging) | 4.0.4 |
| Lombok | — |
| Maven | — |

### Frontend
| Technology | Version |
|---|---|
| React | 19.2.6 |
| Vite | 8.0.12 |
| Tailwind CSS | 4.3.0 |
| DaisyUI | 5.5.1 |
| Axios | — |
| React Router | 7.15.1 |
| React Hook Form | 7.75.0 |
| Recharts | 3.8.1 |
| Zustand | 5.0.13 |
| date-fns | 4.4.0 |

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Browser                           │
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │               React Frontend (Vite)                     │  │
│   │                                                         │  │
│   │  Zustand ──► Components ──► Axios ──► API calls        │  │
│   │  (state)     (React + TW)   (HTTP)                      │  │
│   └─────────────────────────┬───────────────────────────────┘  │
└─────────────────────────────│───────────────────────────────────┘
                              │  HTTP/JSON  (port 5173 dev)
                              │
┌─────────────────────────────▼───────────────────────────────────┐
│                   Spring Boot Backend (port 8080)               │
│                                                                 │
│  ┌────────────┐   ┌────────────┐   ┌────────────────────────┐  │
│  │  Security  │   │  REST      │   │  Service Layer         │  │
│  │  Filter    │──►│  Controllers│──►│  (Business Logic)      │  │
│  │  (JWT)     │   │  /api/**   │   │                        │  │
│  └────────────┘   └────────────┘   └───────────┬────────────┘  │
│                                                │               │
│  ┌──────────────────────────────────────────── ▼ ────────────┐  │
│  │               Spring Data JPA / Hibernate                  │  │
│  └─────────────────────────────────────────────┬─────────────┘  │
└────────────────────────────────────────────────│────────────────┘
                                                 │  JDBC
                                    ┌────────────▼────────────┐
                                    │       PostgreSQL         │
                                    └─────────────────────────┘
```

### Package Structure (Backend)

```
lt.techin.shiftpilot/
├── datainitializer/         # Seed data on startup
├── exception/               # Global exception handling
│   ├── core/
│   ├── auth/
│   ├── assignment/
│   ├── notification/
│   └── user/
├── feature/
│   ├── auth/                # Login / logout / current user
│   ├── dashboard/           # Manager & employee dashboards
│   ├── leaverequest/        # Leave request creation
│   ├── managerapproval/     # Approval queue & processing
│   ├── notification/        # In-app notifications
│   ├── shift/               # Shift CRUD
│   ├── shiftDraft/          # Reusable shift templates
│   ├── shiftassignment/     # Assigning employees to shifts
│   ├── swaprequest/         # Shift swap workflow
│   └── user/                # User management
├── security/
│   ├── config/              # Security filter chain
│   ├── jwt/                 # Token creation & validation
│   ├── principal/           # UserDetails implementation
│   ├── service/             # Auth services
│   └── exception/           # Auth exceptions
└── telemetry/               # Request telemetry
```

---

## Installation

### Prerequisites

- Java 21
- Maven 3.9+
- Node.js 20+
- PostgreSQL 15+

### Clone the repository

```bash
git clone https://github.com/your-org/shiftpilot.git
cd shiftpilot
```

### Backend setup

```bash
cd backend
mvn clean install
```

### Frontend setup

```bash
cd frontend
npm install
```

---

## Environment Variables

Create a `.env` file or set the following variables in your environment before starting the backend.

| Variable | Description | Example |
|---|---|---|
| `DB_URL` | JDBC connection URL for PostgreSQL | `jdbc:postgresql://localhost:5432/shiftpilot` |
| `DB_USERNAME` | PostgreSQL username | `postgres` |
| `DB_PASSWORD` | PostgreSQL password | `secret` |
| `JWT_SECRET` | Secret key used to sign JWT tokens (min. 256-bit) | `your-very-long-secret-key` |

> **Note:** The backend also reads `frontend.url` from `application.yml` for CORS configuration. It defaults to `http://localhost:5173` — change it in `application.yml` for production.

---

## Running the Application

### Backend

```bash
cd backend

# Set environment variables (or export them beforehand)
export DB_URL=jdbc:postgresql://localhost:5432/shiftpilot
export DB_USERNAME=postgres
export DB_PASSWORD=secret
export JWT_SECRET=your-very-long-secret-key

mvn spring-boot:run
```

The backend starts on **http://localhost:8080**.

### Frontend

```bash
cd frontend
npm run dev
```

The frontend starts on **http://localhost:5173**.

---

## Future Improvements

- [ ] Docker Compose setup for one-command local development
- [ ] CI/CD pipeline (GitHub Actions) with automated tests and Docker image publishing
- [ ] WebSocket or SSE-based real-time notifications
- [ ] Email notifications for shift assignments and request outcomes
- [ ] Calendar export (iCal / .ics) for personal schedules
- [ ] Mobile-responsive PWA or native mobile app
- [ ] Audit log / activity history per user
- [ ] Shift coverage alerts when understaffed

---

## License

This project is private and not currently licensed for public use.
