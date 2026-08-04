# Digital Banking System

A secure and scalable banking backend application built using Spring Boot. The system provides user authentication, account management, transaction processing, fraud monitoring, and role-based access control.

## Features

### Authentication & Authorization

* JWT-based authentication
* Secure login and registration
* Logout with token blacklisting using Redis
* Role-based access control (USER and ADMIN)

### User Management

* User registration
* Profile retrieval
* User profile updates
* Fetch users by ID or email

### Account Management

* Create bank accounts
* Support for multiple accounts per user
* Retrieve account details
* Retrieve accounts associated with a user
* Account status management
* Account type support

### Transaction Management

* Money transfers between accounts
* Deposit funds
* Withdraw funds
* Transaction history
* Transaction reference tracking
* Transaction status management

### Fraud Monitoring

* Fraud log generation
* Fraud status tracking
* Fraud review workflow
* Administrative fraud management endpoints

### Security

* Spring Security integration
* JWT token validation
* Protected API endpoints
* Role-based endpoint authorization
* Token revocation through Redis

---

## Tech Stack

### Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Database

* MySQL

### Caching / Session Management

* Redis

### Authentication

* JWT (JSON Web Token)

### Utilities

* Lombok
* ModelMapper
* Jakarta Validation
* SLF4J Logging

---

## Project Structure

```text
src/main/java/com/MyProject/DigitalBankingSystem

├── account
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── auth
│   ├── controller
│   ├── dto
│   ├── jwt
│   ├── custom
│   └── service
│
├── transaction
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── fraud
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
│── idempotency
│   ├── service
│
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── config
├── exception
└── util
```

---

## Database Design

### User

Stores customer and administrator information.

| Field     | Type          |
| --------- | ------------- |
| id        | Long          |
| name      | String        |
| email     | String        |
| password  | String        |
| role      | Enum          |
| createdAt | LocalDateTime |

---

### Account

Represents a bank account owned by a user.

| Field         | Type          |
| ------------- | ------------- |
| id            | Long          |
| accountNumber | String        |
| balance       | BigDecimal    |
| status        | Enum          |
| accountType   | Enum          |
| createdAt     | LocalDateTime |

---

### Transaction

Represents transfers, deposits, and withdrawals.

| Field                | Type          |
| -------------------- | ------------- |
| id                   | Long          |
| transactionReference | String        |
| senderAccount        | Account       |
| receiverAccount      | Account       |
| amount               | BigDecimal    |
| transactionType      | Enum          |
| status               | Enum          |
| transactionAt        | LocalDateTime |

---

### FraudLog

Tracks suspicious transactions and fraud investigations.

| Field                | Type          |
| -------------------- | ------------- |
| id                   | Long          |
| accountNumber        | String        |
| transactionReference | String        |
| status               | Enum          |
| reason               | String        |
| detectedAt           | LocalDateTime |
| reviewedAt           | LocalDateTime |

---

## API Modules

### Authentication APIs

```http
POST /auth/register
POST /auth/login
POST /auth/logout
```

### User APIs

```http
GET /users/{userId}
GET /users/email/{email}
GET /users/my-user
PATCH /users/{userId}
```

### Account APIs

```http
POST /accounts
GET /accounts/{accountId}
GET /accounts/account-number/{accountNumber}
GET /accounts/user/{userId}
GET /accounts/my-account/{accountNumber}
GET /accounts/my-accounts
```

### Transaction APIs

```http
POST /transactions/transfer
POST /transactions/deposit
POST /transactions/withdraw

GET /transactions/{transactionId}
GET /transactions/reference/{transactionReference}
GET /transactions/all-transactions/{accountNumber}
```

### Fraud APIs

```http
GET /fraud-logs
GET /fraud-logs/{fraudLogId}
PATCH /fraud-logs/{fraudLogId}/status
```

---
## Dockerized Deployment

The application is fully containerized using Docker and Docker Compose.

### Services

| Service | Purpose |
|----------|----------|
| Spring Boot App | Banking API |
| MySQL | Persistent database |
| Redis | Token blacklist and caching |

### Run Using Docker

```bash
docker-compose up --build
```

### Container Architecture

```
┌──────────────────────┐
│   Spring Boot API    │
└──────────┬───────────┘
           │
     ┌─────┴─────┐
     │           │
┌────▼────┐ ┌────▼────┐
│ MySQL   │ │ Redis   │
└─────────┘ └─────────┘
```
---

## Getting Started

### Clone Repository

```bash
git clone https://github.com/Amaantheprogrammer/DigitalBankingSystem.git
cd DigitalBankingSystem
```

### Configure MySQL

Create a database:

```sql
CREATE DATABASE digital_banking_system;
```

Update your database credentials in:

```properties
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/digital_banking_system
spring.datasource.username=root
spring.datasource.password=your_password
```

### Configure Redis

Ensure Redis is running locally.

Example:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

### Run Application

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

---

## Security Flow

1. User registers.
2. User logs in.
3. JWT token is generated.
4. Client sends JWT in Authorization header.
5. JWT filter validates the token.
6. Spring Security authorizes requests based on user roles.
7. Logout adds the token to Redis blacklist.

---

## Highlights

- Built using Java 17 and Spring Boot
- Secured with JWT Authentication and Role-Based Access Control (RBAC)
- Redis-powered token blacklisting for secure logout functionality
- Supports fund transfers, deposits, withdrawals, and account management
- Fraud monitoring and audit logging subsystem
- Dockerized application stack using Docker Compose
- MySQL for persistent storage and Redis for caching/session management
- Centralized exception handling and request validation

---

## Future Enhancements

* Email notifications
* OTP verification
* Beneficiary management
* Scheduled payments
* Account statements (PDF export)
* Interest calculation engine
* Multi-factor authentication
* Kafka-based transaction event streaming
* Real-time fraud detection
* Banking analytics dashboard

---

## Author

Amaan Coatwala

Backend Developer focused on Java, Spring Boot, Distributed Systems, and Enterprise Application Development.
