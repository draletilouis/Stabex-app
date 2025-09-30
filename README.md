# 🏦 Stabex Banking Application

**Enterprise-grade Spring Boot banking application** built with **Hexagonal Architecture** and **Domain-Driven Design** principles, featuring comprehensive security, API versioning, event-driven architecture, and production-ready features.

## 🏗️ Architecture Overview

This application implements **Hexagonal Architecture (Ports & Adapters)** combined with **Domain-Driven Design (DDD)**:

```
┌─────────────────────────────────────────────────────────────┐
│                    EXTERNAL WORLD                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Web API   │  │  Database   │  │   Events    │        │
│  │  (HTTP)     │  │  (MySQL)    │  │ (Message    │        │
│  │             │  │             │  │  Broker)    │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
           │                    │                    │
           ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────┐
│                INFRASTRUCTURE LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │ Controllers │  │ Repositories│  │ Publishers  │        │
│  │   (Adapters)│  │  (Adapters) │  │  (Adapters) │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
           │                    │                    │
           ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────┐
│                 APPLICATION LAYER                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Use Cases │  │   Services  │  │   Commands  │        │
│  │   (Ports)   │  │             │  │ Responses   │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
           │                    │                    │
           ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────┐
│                   DOMAIN LAYER                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │  Entities   │  │ Value Objs  │  │  Events     │        │
│  │  (Business  │  │  (Business  │  │ (Business   │        │
│  │   Logic)    │  │   Rules)    │  │  Events)    │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
└─────────────────────────────────────────────────────────────┘
```

### 🎯 Layer Responsibilities

- **Domain Layer**: Pure business logic, entities, value objects, domain events
- **Application Layer**: Use cases, services, commands/responses, ports (interfaces)
- **Infrastructure Layer**: Database persistence, web controllers, external integrations
- **DTO Layer**: API contracts and versioning

## ✨ Key Features

### 🏛️ **Clean Architecture & DDD**
- ✅ **Hexagonal Architecture** with ports and adapters
- ✅ **Domain-Driven Design** with rich domain models
- ✅ **Value Objects** for type safety and business rules
- ✅ **Domain Events** for event-driven architecture
- ✅ **Aggregate Roots** with business logic encapsulation

### 🔒 **Security & Data Protection**
- ✅ **Data Hashing** - Sensitive data never stored in plain text
- ✅ **Hash-based Queries** - Database searches use hashes, not plain values
- ✅ **Input Validation** - Multiple layers of validation (DTO, Domain, Database)
- ✅ **AES Encryption** - Configurable encryption for sensitive data
- ✅ **Optimistic Locking** - Prevents concurrent modification conflicts

### 🚀 **API Versioning & Backward Compatibility**
- ✅ **V1 API** - Classic camelCase endpoints (deprecated with migration path)
- ✅ **V2 API** - Enhanced snake_case endpoints with additional features
- ✅ **Deprecation Headers** - Clear migration guidance for V1 users
- ✅ **Backward Compatibility** - V1 remains functional during transition

### 🛡️ **Reliability & Concurrency**
- ✅ **Idempotency Support** - Prevents duplicate operations
- ✅ **Optimistic Locking** - Version-based concurrency control
- ✅ **Transaction Management** - ACID compliance with Spring transactions
- ✅ **Scheduled Maintenance** - Automatic cleanup of expired data
- ✅ **Comprehensive Error Handling** - RFC 7807 compliant error responses

### 📊 **Monitoring & Observability**
- ✅ **Structured Logging** - Comprehensive audit trails
- ✅ **Trace ID Support** - Request correlation for debugging
- ✅ **Domain Events** - Event publishing for integration
- ✅ **Health Checks** - Application monitoring capabilities

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **MySQL 8.0+**
- **Gradle** (wrapper included)

### 1. Database Setup
```sql
CREATE DATABASE banking_db;
```

### 2. Configuration
Update `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run Application
```bash
./gradlew bootRun --args="--server.port=8080"
```

Application will be available at `http://localhost:8080`

### 4. API Documentation
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`

## 📋 API Endpoints

### 🎯 V1 API (Deprecated)
**Base URL**: `/api/v1/accounts`
**Status**: Deprecated (Sunset: 2025-12-31)

#### Create Account
```http
POST /api/v1/accounts
Content-Type: application/json

{
  "accountHolderName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "accountType": "CHECKING"
}
```

#### Get Account
```http
GET /api/v1/accounts/{accountNumber}
```

#### Deposit Money
```http
POST /api/v1/accounts/{accountNumber}/deposit
Content-Type: application/json

{
  "amount": 250.00,
  "description": "Salary deposit",
  "idempotencyKey": "deposit-001"
}
```

#### Withdraw Money
```http
POST /api/v1/accounts/{accountNumber}/withdraw
Content-Type: application/json

{
  "amount": 50.00,
  "description": "ATM withdrawal",
  "idempotencyKey": "withdraw-001"
}
```

### 🚀 V2 API (Current)
**Base URL**: `/api/v2/accounts`
**Status**: Current version with enhanced features

#### Create Account (Enhanced)
```http
POST /api/v2/accounts
Content-Type: application/json

{
  "holder_name": "Jane Smith",
  "email_address": "jane.smith@example.com",
  "phone": "+15550002222",
  "account_type": "SAVINGS",
  "account_holder_id": "AH-0001",
  "branch_code": "BR001",
  "currency": "USD",
  "initial_deposit": 200.00,
  "interest_rate": 2.5
}
```

#### Enhanced Transactions
```http
POST /api/v2/accounts/{accountNumber}/deposit
Content-Type: application/json

{
  "amount": 150.00,
  "description": "V2 enhanced deposit",
  "transaction_reference": "TXN-001",
  "category": "SALARY",
  "tags": "monthly,bonus",
  "external_reference": "EXT-REF-001",
  "idempotency_key": "v2-deposit-001"
}
```

## 🏗️ Domain Model

### Core Entities
- **`Account`** - Main aggregate root with business logic
- **`IdempotencyKey`** - Prevents duplicate operations

### Value Objects
- **`Money`** - Prevents floating-point errors, handles currency
- **`AccountNumber`** - Unique account identifier with hashing
- **`Email`** - Validated email with privacy hashing
- **`PhoneNumber`** - Optional phone with validation
- **`AccountHolderName`** - Validated name requirements
- **`AccountId`** - Database ID wrapper
- **`Version`** - Optimistic locking support

### Domain Events
- **`AccountCreatedEvent`** - Published when account is created
- **`DepositCompletedEvent`** - Published when deposit succeeds

### Business Rules
- **Account Status Validation** - Only active accounts can transact
- **Sufficient Funds Check** - Withdrawal amount validation
- **Unique Constraints** - Email and account number uniqueness
- **Concurrent Modification** - Optimistic locking prevents conflicts

## 🔧 Architecture Components

### Domain Layer (`src/main/java/com/demo/banking_app/domain/`)
```
domain/
├── model/
│   ├── Account.java                 # Main aggregate root
│   ├── AccountNumber.java           # Value object with hashing
│   ├── Money.java                   # Money calculations
│   ├── Email.java                   # Email validation
│   ├── AccountCreatedEvent.java     # Domain events
│   └── ...
└── exception/
    ├── DomainException.java         # Base domain exception
    ├── InsufficientFundsException.java
    └── ...
```

### Application Layer (`src/main/java/com/demo/banking_app/application/`)
```
application/
├── port/
│   ├── in/                          # Inbound ports (use cases)
│   │   ├── CreateAccountUseCase.java
│   │   ├── DepositUseCase.java
│   │   └── ...
│   └── out/                         # Outbound ports (dependencies)
│       ├── AccountRepository.java
│       ├── EventPublisher.java
│       └── ...
└── service/                         # Use case implementations
    ├── CreateAccountService.java
    ├── DepositService.java
    └── ...
```

### Infrastructure Layer (`src/main/java/com/demo/banking_app/infrastructure/`)
```
infrastructure/
├── persistence/
│   ├── AccountEntity.java           # JPA entity
│   ├── AccountMapper.java           # Domain ↔ Entity mapping
│   ├── JpaAccountRepository.java    # Repository implementation
│   └── ...
├── web/
│   ├── v1/AccountControllerV1.java  # V1 API (deprecated)
│   └── v2/AccountControllerV2.java  # V2 API (current)
└── util/
    └── JsonUtils.java               # JSON utilities
```

## 🛡️ Security Features

### Data Protection
- **Hash-based Storage**: Account numbers, emails, phone numbers stored as hashes
- **Hash-based Queries**: Database searches use hashes, not plain values
- **Encryption Support**: AES-GCM encryption for sensitive data
- **Input Validation**: Multiple validation layers

### Concurrency Control
- **Optimistic Locking**: Version field prevents lost updates
- **Idempotency**: Duplicate operation prevention
- **Transaction Safety**: ACID compliance

### Error Handling
- **RFC 7807 Compliance**: Standardized error responses
- **Domain Exceptions**: Business-meaningful error types
- **Trace IDs**: Request correlation for debugging

## 📊 Response Examples

### Successful Account Creation
```json
{
  "id": 1,
  "accountNumber": "1234567890",
  "accountHolderName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "balance": 0.00,
  "accountType": "SAVINGS",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Successful Transaction
```json
{
  "accountNumber": "1234567890",
  "transactionType": "DEPOSIT",
  "amount": 1000.00,
  "newBalance": 1000.00,
  "description": "Initial deposit",
  "idempotencyKey": "deposit-1234567890-2024-01-15-001",
  "timestamp": "2024-01-15T10:35:00"
}
```

### Error Response (RFC 7807)
```json
{
  "type": "about:blank",
  "title": "Insufficient Funds",
  "status": 400,
  "detail": "Insufficient funds. Current balance: 100.00, Requested amount: 500.00",
  "instance": "/api/v2/accounts/1234567890/withdraw",
  "errorCode": "INSUFFICIENT_FUNDS",
  "currentBalance": 100.00,
  "requestedAmount": 500.00,
  "traceId": "req-123456789"
}
```

## 🧪 Testing

### API Testing Examples
```bash
# Create Account (V2)
curl -X POST http://localhost:8080/api/v2/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "holder_name": "Test User",
    "email_address": "test@example.com",
    "phone": "+1234567890",
    "account_type": "CHECKING",
    "account_holder_id": "TEST001",
    "branch_code": "BR001",
    "currency": "USD",
    "initial_deposit": 100.00
  }'

# Deposit with Idempotency
curl -X POST http://localhost:8080/api/v2/accounts/{accountNumber}/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 250.00,
    "description": "Test deposit",
    "idempotency_key": "test-deposit-001"
  }'
```

## 🔧 Configuration

### Environment Variables
```properties
# Database
spring.datasource.username=your_username
spring.datasource.password=your_password

# Security (Production)
APP_CRYPTO_KEY=<Base64-encoded-32-byte-key>
APP_CRYPTO_SALT=<Base64-encoded-HMAC-salt>

# Server
server.port=8080

# API Documentation
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
```

### Scheduled Tasks
- **Idempotency Cleanup**: Runs every hour to remove expired keys
- **Maintenance Tasks**: Automated cleanup and health checks

## 📈 Performance & Scalability

### Optimizations
- **Hash-based Indexing**: Fast lookups on sensitive data
- **Optimistic Locking**: No database locks for better concurrency
- **Connection Pooling**: Efficient database connection management
- **Lazy Loading**: JPA lazy loading for related entities

### Monitoring
- **Structured Logging**: JSON-formatted logs for analysis
- **Metrics**: Application metrics and health indicators
- **Trace IDs**: Distributed tracing support

## 🎯 Account Types & Status

### Account Types
- **`SAVINGS`** - Interest-bearing savings account
- **`CHECKING`** - Transaction account for daily banking
- **`BUSINESS`** - Business account with enhanced features

### Account Status
- **`ACTIVE`** - Account can perform transactions
- **`INACTIVE`** - Account is disabled
- **`SUSPENDED`** - Account temporarily suspended

## 🚀 Recent Updates

### Latest Implementation (2024)
- ✅ **Complete Hexagonal Architecture** - Migrated to ports and adapters pattern
- ✅ **Domain-Driven Design** - Rich domain models with business logic
- ✅ **Security Hardening** - Hash-based data protection and encryption
- ✅ **API Versioning** - V1 deprecation with V2 enhancement
- ✅ **Event-Driven Architecture** - Domain events for integration
- ✅ **Production Readiness** - Comprehensive error handling and monitoring
- ✅ **Concurrency Safety** - Optimistic locking and idempotency
- ✅ **Maintainability** - Clean code structure and separation of concerns

### Architecture Benefits
- **Testability** - Easy to unit test business logic in isolation
- **Maintainability** - Clear boundaries and single responsibility
- **Flexibility** - Easy to swap implementations (databases, frameworks)
- **Scalability** - Loose coupling enables horizontal scaling
- **Security** - Multiple layers of data protection

---

**🏦 Built with Spring Boot 3, Hibernate 6, MySQL 8, Clean Architecture, Domain-Driven Design, and enterprise-grade security practices.**