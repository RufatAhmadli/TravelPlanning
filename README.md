# TravelPlanning

A full-stack travel-planning application built around a secure Spring Boot REST API. Users can register and authenticate, create and browse trips, manage addresses, leave trip reviews, and receive real-time notifications. The project follows a clean, layered architecture with DTO mapping, database migrations, and interactive API documentation.

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.0-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-7-646CFF?logo=vite&logoColor=white)

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [API Overview](#api-overview)
- [Real-Time Notifications](#real-time-notifications)
- [Database & Migrations](#database--migrations)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Author](#author)

## Features

- **Authentication & authorization** — user registration and login with stateless **JWT** tokens, backed by Spring Security.
- **Trip management** — create trips, search by destination or departure time, and associate trips with users.
- **Reviews** — users can review trips, with lookups by user or by trip.
- **Address management** — multiple address types per user, searchable by city, street, or type.
- **Roles** — role entities managed through a dedicated endpoint and assignable to users.
- **Real-time notifications** — WebSocket (STOMP) channel for pushing notifications to clients.
- **Interactive API docs** — OpenAPI 3 / Swagger UI generated from the code.
- **Robust engineering** — DTOs with MapStruct mapping, bean validation with validation groups, JPA auditing, centralized exception handling, and versioned database schema via Liquibase.

## Tech Stack

**Backend**
- Java 17, Spring Boot 3.5.0
- Spring Web, Spring Data JPA, Spring Security, Spring Validation
- JSON Web Tokens (jjwt 0.13.0)
- Spring WebSocket (STOMP messaging)
- MapStruct 1.6.2 (DTO ↔ entity mapping), Lombok
- Liquibase 4.32.0 (database migrations)
- springdoc-openapi 2.7.0 (Swagger UI)
- Spring Boot Actuator
- PostgreSQL (runtime), H2 (tests)
- Maven (with wrapper)

**Frontend**
- React 19 + Vite 7 *(early stage — see [Roadmap](#roadmap))*

## Architecture

The backend uses a conventional layered design so that responsibilities stay separated and testable:

```
Controller  ->  Service  ->  Repository  ->  Database
     |             |
   DTOs        MapStruct mappers
```

- **Controllers** expose the REST/WebSocket endpoints and handle request/response DTOs.
- **Services** hold business logic.
- **Repositories** (Spring Data JPA) handle persistence.
- **Entities** extend a shared `BaseEntity` that provides JPA auditing (created/updated metadata).
- **Cross-cutting concerns**: JWT filter and security configuration, a global exception handler with custom exceptions and a consistent `ErrorResponse`, and validation groups (`OnCreate`, `OnUpdate`, `OnRegister`).

## Project Structure

```
TravelPlanning/
├── src/main/java/edu/az/example/web/travelplanning/
│   ├── config/         # Security, Swagger/OpenAPI, WebSocket, JPA auditing
│   ├── controller/     # REST + WebSocket endpoints
│   ├── dto/            # Request/response models
│   ├── enums/          # AddressType, Gender, NotificationType
│   ├── exception/      # Global handler + custom exceptions
│   ├── mapper/         # MapStruct mappers
│   ├── model/entity/   # JPA entities (User, Role, Trip, TripReview, Address, Notification)
│   ├── repository/     # Spring Data JPA repositories
│   ├── security/       # JWT filter, entry point, user security
│   ├── service/        # Business logic
│   └── validation/     # Validation groups
├── src/main/resources/
│   ├── application.yml
│   └── db/changelog/   # Liquibase changelogs
├── src/test/java/...   # Repository & service tests
├── frontend/           # React + Vite client
└── pom.xml
```

## Getting Started

### Prerequisites

- JDK 17+
- PostgreSQL 13+ (running locally or reachable)
- Node.js 18+ and npm (only needed for the frontend)

### 1. Clone

```bash
git clone https://github.com/RufatAhmadli/TravelPlanning.git
cd TravelPlanning
```

### 2. Create the database

```sql
CREATE DATABASE travelplanning;
```

### 3. Configure environment variables

The datasource is read from environment variables (see [Configuration](#configuration)):

```bash
export JDBC_URL=jdbc:postgresql://localhost:5432/travelplanning
export JDBC_USERNAME=postgres
export JDBC_PASSWORD=your_password
```

### 4. Run the backend

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. Liquibase applies the schema automatically on startup.

### 5. Run the frontend (optional)

```bash
cd frontend
npm install
npm run dev
```

## Configuration

| Variable        | Description                          | Example                                          |
| --------------- | ------------------------------------ | ------------------------------------------------ |
| `JDBC_URL`      | PostgreSQL JDBC URL                  | `jdbc:postgresql://localhost:5432/travelplanning` |
| `JDBC_USERNAME` | Database username                    | `postgres`                                       |
| `JDBC_PASSWORD` | Database password                    | `your_password`                                  |

Hibernate runs with `ddl-auto: validate`, so the schema is owned and versioned by Liquibase rather than auto-generated by JPA.

## API Documentation

Once the app is running, the interactive docs are available at:

- **Swagger UI** — `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI spec** — `http://localhost:8080/v3/api-docs`

## API Overview

All endpoints are prefixed with `/api/v1`. Protected endpoints require an `Authorization: Bearer <token>` header obtained from `/authenticate`.

### Authentication

| Method | Endpoint        | Description                     |
| ------ | --------------- | ------------------------------- |
| POST   | `/register`     | Register a new user             |
| POST   | `/authenticate` | Log in and receive a JWT        |

### Users — `/api/v1/users`

| Method | Endpoint            | Description               |
| ------ | ------------------- | ------------------------- |
| GET    | `/`                 | List all users            |
| GET    | `/{id}`             | Get user by ID            |
| GET    | `/gender/{gender}`  | List users by gender      |
| POST   | `/`                 | Create a user             |
| PUT    | `/{userId}/role`    | Assign a role to a user   |
| PUT    | `/{id}`             | Update a user             |
| PATCH  | `/{id}`             | Partially update a user   |
| DELETE | `/{id}`             | Delete a user             |

### Trips — `/api/v1/trips`

| Method | Endpoint                        | Description                     |
| ------ | ------------------------------- | ------------------------------- |
| GET    | `/`                             | List all trips                  |
| GET    | `/{id}`                         | Get trip by ID                  |
| GET    | `/destination/{destination}`    | Find trips by destination       |
| GET    | `/time/{departureTime}`         | Find trips by departure time    |
| GET    | `/users/{userId}`               | List a user's trips             |
| POST   | `/`                             | Create a trip                   |
| POST   | `/{tripId}/addTrip/{userId}`    | Link an existing trip to a user |
| PUT    | `/{id}`                         | Update a trip                   |
| DELETE | `/{id}`                         | Delete a trip                   |

### Reviews — `/api/v1/reviews`

| Method | Endpoint            | Description                          |
| ------ | ------------------- | ------------------------------------ |
| GET    | `/{id}`             | Get review by ID                     |
| GET    | `/user/{userId}`    | List reviews by user                 |
| GET    | `/trip/{tripId}`    | List reviews by trip                 |
| POST   | `/?tripId={tripId}` | Create a review for a trip           |
| PUT    | `/{id}`             | Update a review                      |
| DELETE | `/{id}`             | Delete a review                      |

### Addresses — `/api/v1/addresses`

| Method | Endpoint                          | Description                         |
| ------ | --------------------------------- | ----------------------------------- |
| GET    | `/`                               | List all addresses                  |
| GET    | `/{id}`                           | Get address by ID                   |
| GET    | `/city/{city}`                    | Find addresses by city              |
| GET    | `/addressType/{addressType}`      | Find addresses by type              |
| GET    | `/street?street={s}&number={n}`   | Find addresses by street and number |
| GET    | `/userId/{id}`                    | List a user's addresses             |
| POST   | `/`                               | Create an address                   |
| PUT    | `/{id}`                           | Update an address                   |
| DELETE | `/{id}`                           | Delete an address                   |

### Roles — `/api/v1/roles`

| Method | Endpoint  | Description        |
| ------ | --------- | ------------------ |
| GET    | `/`       | List all roles     |
| GET    | `/{id}`   | Get role by ID     |
| POST   | `/`       | Create a role      |
| PUT    | `/{id}`   | Update a role      |
| DELETE | `/{id}`   | Delete a role      |

**Enumerations**
- `AddressType`: `HOME`, `WORK`, `BILLING`, `OTHER`
- `Gender`: `MALE`, `FEMALE`, `OTHER`
- `NotificationType`: `BOOKING_REMINDER`, `BOOKING_STATUS_UPDATE`, `REVIEW`, `PROMOTION`, `ALERT`

## Real-Time Notifications

The service exposes a STOMP-over-WebSocket channel:

- **Connect endpoint:** `/websocket`
- **Application prefix:** `/app`
- **Broker (subscribe) prefix:** `/topic`

Send a message to `/app/sendMessage` and subscribers of `/topic/notifications` receive it.

## Database & Migrations

Schema changes are managed with **Liquibase**. The master changelog is at
`src/main/resources/db/changelog/db.changelog-master.yaml`, which includes the
dated changesets under `db/changelog/logs/`. Migrations run automatically at
application startup.

## Testing

Tests run against an in-memory **H2** database using the `test` profile:

```bash
./mvnw test
```

Coverage includes repository-layer and service-layer tests
(`AddressServiceTest`, `TripServiceTest`, `UserServiceTest`, and the repository tests).

## Roadmap

- Flesh out the React frontend (routing, auth flow, trip/review UI) — currently a Vite starter scaffold.
- Expand notification delivery beyond the demo WebSocket echo.
- Add integration and end-to-end tests.
- Add a license.

## Author

**Rufat Ahmadli** — Computer Engineering student, Baku Higher Oil School
GitHub: [@RufatAhmadli](https://github.com/RufatAhmadli)
