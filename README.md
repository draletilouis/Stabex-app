# Stabex Banking Application

**Enterprise-grade Spring Boot banking application** built with **Hexagonal Architecture** and **Domain-Driven Design** principles, featuring comprehensive security, API versioning, event-driven architecture, and production-ready features.

## Architecture Overview

This application implements **Hexagonal Architecture (Ports & Adapters)** combined with **Domain-Driven Design (DDD)** to ensure maintainability, testability, and flexibility.

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

### Layer Responsibilities

- **Domain Layer**: Pure business logic, entities, value objects, domain events - the heart of the application containing all business rules
- **Application Layer**: Use cases, services, commands/responses, ports (interfaces) - orchestrates domain objects to fulfill use cases
- **Infrastructure Layer**: Database persistence, web controllers, external integrations - technical implementation details
- **DTO Layer**: API contracts and versioning - data transfer objects for external communication

### Architectural Principles

1. **Dependency Inversion**: Dependencies point inward toward the domain layer
2. **Separation of Concerns**: Each layer has a distinct responsibility
3. **Technology Agnostic Domain**: Domain layer has no framework dependencies
4. **Testability**: Business logic can be tested without external dependencies

## Key Features

### Clean Architecture & Domain-Driven Design

- **Hexagonal Architecture** with ports and adapters pattern
- **Domain-Driven Design** with rich domain models and ubiquitous language
- **Value Objects** for type safety and business rules encapsulation
- **Domain Events** for event-driven architecture and loose coupling
- **Aggregate Roots** with business logic encapsulation and consistency boundaries
- **Repository Pattern** for data access abstraction
- **Use Case Driven Development** with clear application service boundaries

### Security & Data Protection

- **Data Hashing** - Sensitive data (account numbers, emails, phone numbers) never stored in plain text
- **Hash-based Queries** - Database searches use SHA-256 hashes for privacy protection
- **Input Validation** - Multiple layers of validation (DTO, Domain, Database constraints)
- **AES-GCM Encryption** - Configurable encryption for highly sensitive data at rest
- **Optimistic Locking** - Version-based concurrency control prevents lost updates
- **Secure Error Messages** - No sensitive data leakage in error responses
- **SQL Injection Prevention** - Parameterized queries and JPA protection

### API Versioning & Evolution Strategy

- **V1 API** - Classic camelCase endpoints (deprecated with clear migration path)
- **V2 API** - Enhanced snake_case endpoints with additional features and improved design
- **Deprecation Headers** - Clear migration guidance for V1 API consumers
- **Backward Compatibility** - V1 remains fully functional during transition period
- **URL Path Versioning** - Clean and explicit versioning strategy
- **API Documentation** - Comprehensive OpenAPI/Swagger documentation for both versions

### Reliability & Concurrency

- **Idempotency Support** - Prevents duplicate operations using idempotency keys
- **Optimistic Locking** - Version-based concurrency control for account updates
- **Transaction Management** - ACID compliance with Spring @Transactional annotations
- **Scheduled Maintenance** - Automatic cleanup of expired idempotency keys
- **Comprehensive Error Handling** - RFC 7807 compliant problem details responses
- **Retry Mechanisms** - Graceful handling of transient failures
- **Audit Trail** - Complete transaction history with timestamps

### Monitoring & Observability

- **Structured Logging** - Comprehensive audit trails with contextual information
- **Trace ID Support** - Request correlation for distributed tracing and debugging
- **Domain Events** - Event publishing for system integration and monitoring
- **Health Checks** - Spring Boot Actuator endpoints for application monitoring
- **Metrics Collection** - Performance and business metrics tracking
- **Error Tracking** - Detailed error logging with stack traces

## Quick Start

### Prerequisites

- **Java 17+** (OpenJDK or Oracle JDK)
- **MySQL 8.0+** (or MariaDB 10.6+)
- **Gradle 8.0+** (wrapper included in project)
- **Git** for version control

### 1. Database Setup

Create the database and configure proper permissions:

```sql
CREATE DATABASE banking_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Optional: Create dedicated user
CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON banking_db.* TO 'banking_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configuration

Update database connection settings in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=banking_user
spring.datasource.password=secure_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Server Configuration
server.port=8080
```

### 3. Build the Application

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test
```

### 4. Run the Application

```bash
# Using Gradle wrapper
./gradlew bootRun --args="--server.port=8080"

# Or using the JAR
java -jar build/libs/banking-app-0.0.1-SNAPSHOT.jar
```

The application will start at `http://localhost:8080`

### 5. API Documentation

Once running, access the interactive API documentation:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Specification**: `http://localhost:8080/v3/api-docs`
- **Health Check**: `http://localhost:8080/actuator/health`

## API Endpoints

### V1 API (Deprecated)

**Base URL**: `/api/v1/accounts`
**Status**: Deprecated (Planned Sunset: 2025-12-31)
**Features**: Basic banking operations with camelCase naming

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

**Response**: `201 Created`
```json
{
  "id": 1,
  "accountNumber": "1234567890",
  "accountHolderName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "balance": 0.00,
  "accountType": "CHECKING",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### Get Account

```http
GET /api/v1/accounts/{accountNumber}
```

**Response**: `200 OK` with account details

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

**Response**: `200 OK`
```json
{
  "accountNumber": "1234567890",
  "transactionType": "DEPOSIT",
  "amount": 250.00,
  "newBalance": 250.00,
  "description": "Salary deposit",
  "idempotencyKey": "deposit-001",
  "timestamp": "2024-01-15T10:35:00"
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

### V2 API (Current)

**Base URL**: `/api/v2/accounts`
**Status**: Current stable version
**Features**: Enhanced functionality with snake_case naming, additional fields, improved validation

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

**New Features in V2**:
- Additional metadata fields (account_holder_id, branch_code, currency)
- Initial deposit support during account creation
- Interest rate configuration for savings accounts
- Enhanced validation and business rules

#### Get Account (Enhanced)

```http
GET /api/v2/accounts/{accountNumber}
```

**Response**: Includes additional V2 fields

#### Enhanced Transactions

**Deposit**:
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

**Enhanced Features**:
- Transaction categorization
- Tag support for transaction classification
- External reference tracking for reconciliation
- Transaction reference numbers

**Withdraw**:
```http
POST /api/v2/accounts/{accountNumber}/withdraw
Content-Type: application/json

{
  "amount": 75.00,
  "description": "V2 enhanced withdrawal",
  "transaction_reference": "TXN-002",
  "category": "CASH",
  "tags": "atm",
  "idempotency_key": "v2-withdraw-001"
}
```

## Domain Model

### Core Entities

#### Account (Aggregate Root)
The main entity representing a bank account with complete business logic:

```java
public class Account {
    private AccountId id;
    private AccountNumber accountNumber;
    private AccountHolderName accountHolderName;
    private Email email;
    private Optional<PhoneNumber> phoneNumber;
    private Money balance;
    private AccountType accountType;
    private AccountStatus status;
    private Version version;  // For optimistic locking

    // Business methods
    public void deposit(Money amount, String description);
    public void withdraw(Money amount, String description);
    public void activate();
    public void suspend();
}
```

#### IdempotencyKey
Prevents duplicate operations by tracking processed requests:

```java
public class IdempotencyKey {
    private String key;
    private String operation;
    private LocalDateTime expiresAt;
    private boolean processed;
}
```

### Value Objects

Value objects are immutable and defined by their attributes, providing type safety and encapsulating business rules:

#### Money
Prevents floating-point arithmetic errors and handles currency operations:

```java
public class Money {
    private final BigDecimal amount;
    private final Currency currency;

    // Arithmetic operations
    public Money add(Money other);
    public Money subtract(Money other);
    public boolean isGreaterThan(Money other);
}
```

#### AccountNumber
Unique account identifier with built-in hashing for privacy:

```java
public class AccountNumber {
    private final String value;
    private final String hashedValue;  // SHA-256 hash
}
```

#### Email
Validated email address with privacy protection:

```java
public class Email {
    private final String value;
    private final String hashedValue;

    // Validation rules
    - Must be valid email format
    - Maximum 255 characters
    - Stored as hash in database
}
```

#### PhoneNumber
Optional validated phone number:

```java
public class PhoneNumber {
    private final String value;
    private final String hashedValue;

    // Validation rules
    - International format (E.164)
    - Optional field
    - Privacy-protected storage
}
```

#### AccountHolderName
Validated account holder name:

```java
public class AccountHolderName {
    private final String value;

    // Validation rules
    - Minimum 2 characters
    - Maximum 100 characters
    - Non-empty required
}
```

#### AccountId
Database identifier wrapper:

```java
public class AccountId {
    private final Long value;
}
```

#### Version
Optimistic locking version control:

```java
public class Version {
    private final Long value;

    public Version increment();
}
```

### Domain Events

Events published when significant business actions occur:

#### AccountCreatedEvent
```java
public class AccountCreatedEvent extends DomainEvent {
    private final AccountNumber accountNumber;
    private final AccountHolderName holderName;
    private final Email email;
    private final AccountType accountType;
    private final LocalDateTime occurredAt;
}
```

#### DepositCompletedEvent
```java
public class DepositCompletedEvent extends DomainEvent {
    private final AccountNumber accountNumber;
    private final Money amount;
    private final Money newBalance;
    private final String description;
    private final LocalDateTime occurredAt;
}
```

#### WithdrawalCompletedEvent
```java
public class WithdrawalCompletedEvent extends DomainEvent {
    private final AccountNumber accountNumber;
    private final Money amount;
    private final Money newBalance;
    private final String description;
    private final LocalDateTime occurredAt;
}
```

### Business Rules

The domain layer enforces critical business rules:

1. **Account Status Validation**
   - Only ACTIVE accounts can perform transactions
   - SUSPENDED accounts cannot transact but can be queried
   - INACTIVE accounts are closed

2. **Sufficient Funds Check**
   - Withdrawal amount must not exceed current balance
   - Minimum balance requirements enforced
   - Overdraft protection

3. **Unique Constraints**
   - Email must be unique across all accounts
   - Account number must be globally unique
   - Enforced at both domain and database level

4. **Concurrent Modification Protection**
   - Optimistic locking prevents lost updates
   - Version checking on every update
   - Retry logic for version conflicts

5. **Data Integrity**
   - All monetary amounts must be non-negative
   - Email and phone validation
   - Account type and status validation

6. **Idempotency**
   - Duplicate operations prevented within expiration window
   - Same idempotency key returns same result
   - Automatic cleanup of expired keys

## Architecture Components

### Domain Layer

**Location**: `src/main/java/com/demo/banking_app/domain/`

```
domain/
├── model/
│   ├── Account.java                    # Main aggregate root with business logic
│   ├── AccountNumber.java              # Value object with privacy hashing
│   ├── Money.java                      # Money calculations and currency
│   ├── Email.java                      # Email validation and hashing
│   ├── PhoneNumber.java                # Optional phone with validation
│   ├── AccountHolderName.java          # Name validation
│   ├── AccountId.java                  # Database ID wrapper
│   ├── Version.java                    # Optimistic locking version
│   ├── AccountType.java                # Enum: SAVINGS, CHECKING, BUSINESS
│   ├── AccountStatus.java              # Enum: ACTIVE, INACTIVE, SUSPENDED
│   ├── AccountCreatedEvent.java        # Domain event
│   ├── DepositCompletedEvent.java      # Domain event
│   ├── WithdrawalCompletedEvent.java   # Domain event
│   ├── DomainEvent.java                # Base event class
│   └── IdempotencyKey.java             # Idempotency tracking
└── exception/
    ├── DomainException.java            # Base domain exception
    ├── InsufficientFundsException.java # Business rule violation
    ├── AccountNotFoundException.java   # Entity not found
    ├── DuplicateEmailException.java    # Uniqueness violation
    ├── IdempotencyException.java       # Duplicate operation
    ├── InvalidAccountStateException.java
    └── ConcurrentModificationException.java
```

**Responsibilities**:
- Pure business logic with no framework dependencies
- Domain entities and value objects
- Business rule enforcement
- Domain events for significant state changes

### Application Layer

**Location**: `src/main/java/com/demo/banking_app/application/`

```
application/
├── port/
│   ├── in/                             # Inbound ports (use cases)
│   │   ├── CreateAccountUseCase.java   # Account creation interface
│   │   ├── DepositUseCase.java         # Deposit operation interface
│   │   ├── WithdrawUseCase.java        # Withdrawal operation interface
│   │   └── GetAccountUseCase.java      # Account query interface
│   └── out/                            # Outbound ports (dependencies)
│       ├── AccountRepository.java      # Persistence interface
│       ├── EventPublisher.java         # Event publishing interface
│       └── IdempotencyRepository.java  # Idempotency storage interface
└── service/                            # Use case implementations
    ├── CreateAccountService.java       # Implements CreateAccountUseCase
    ├── CreateAccountCommand.java       # Input DTO
    ├── CreateAccountResponse.java      # Output DTO
    ├── DepositService.java             # Implements DepositUseCase
    ├── DepositCommand.java             # Input DTO
    ├── DepositResponse.java            # Output DTO
    ├── WithdrawService.java            # Implements WithdrawUseCase
    ├── WithdrawCommand.java            # Input DTO
    ├── WithdrawResponse.java           # Output DTO
    ├── GetAccountService.java          # Implements GetAccountUseCase
    ├── GetAccountCommand.java          # Input DTO
    └── GetAccountResponse.java         # Output DTO
```

**Responsibilities**:
- Orchestrate domain objects to fulfill use cases
- Transaction management
- Input validation and transformation
- Coordinate with repositories and external services

### Infrastructure Layer

**Location**: `src/main/java/com/demo/banking_app/infrastructure/`

```
infrastructure/
├── persistence/
│   ├── AccountEntity.java              # JPA entity mapping
│   ├── IdempotencyKeyEntity.java       # JPA entity for idempotency
│   ├── AccountMapper.java              # Domain ↔ Entity mapping
│   ├── IdempotencyKeyMapper.java       # Domain ↔ Entity mapping
│   ├── AccountJpaRepository.java       # Spring Data JPA repository
│   ├── IdempotencyJpaRepository.java   # Spring Data JPA repository
│   ├── JpaAccountRepository.java       # Repository adapter implementation
│   ├── JpaIdempotencyRepository.java   # Idempotency adapter implementation
│   └── NoOpEventPublisher.java         # Event publisher stub
├── web/
│   ├── v1/
│   │   ├── AccountControllerV1.java    # V1 REST endpoints (deprecated)
│   │   ├── CreateAccountRequestV1.java # V1 DTOs
│   │   └── ...
│   ├── v2/
│   │   ├── AccountControllerV2.java    # V2 REST endpoints (current)
│   │   ├── CreateAccountRequestV2.java # V2 DTOs with enhancements
│   │   └── ...
│   └── common/
│       ├── GlobalExceptionHandler.java # RFC 7807 error handling
│       └── ProblemDetail.java          # Error response format
├── config/
│   ├── SecurityConfig.java             # Security configuration
│   ├── SwaggerConfig.java              # API documentation config
│   └── SchedulerConfig.java            # Scheduled tasks configuration
├── scheduler/
│   └── IdempotencyCleanupScheduler.java # Automated cleanup task
└── util/
    ├── HashUtils.java                  # SHA-256 hashing utilities
    ├── EncryptionUtils.java            # AES encryption utilities
    └── JsonUtils.java                  # JSON processing utilities
```

**Responsibilities**:
- Framework-specific implementations
- Database access and ORM mapping
- REST API endpoints and DTOs
- External system integrations
- Technical concerns (logging, monitoring, etc.)

## Security Features

### Data Protection Strategy

#### Hash-based Storage
All sensitive identifiable information is hashed before storage:

```java
// Account numbers stored as SHA-256 hashes
accountEntity.setAccountNumberHash(hashValue(accountNumber));

// Email addresses hashed for privacy
accountEntity.setEmailHash(hashValue(email));

// Phone numbers optionally hashed
accountEntity.setPhoneNumberHash(phoneNumber.map(this::hashValue));
```

**Benefits**:
- Prevents data exposure in case of database breach
- Enables secure searching without exposing plain values
- Irreversible one-way transformation

#### Hash-based Queries
Database queries use hashes instead of plain values:

```java
// Find account by hashed account number
Optional<Account> account = repository.findByAccountNumberHash(
    HashUtils.sha256(accountNumber)
);
```

#### Encryption Support
Optional AES-GCM encryption for highly sensitive data:

```properties
# Configure encryption keys
APP_CRYPTO_KEY=<Base64-encoded-32-byte-AES-key>
APP_CRYPTO_SALT=<Base64-encoded-HMAC-salt>
```

**Features**:
- AES-GCM authenticated encryption
- Configurable encryption keys via environment variables
- Separate encryption layer from hashing

### Concurrency Control

#### Optimistic Locking
Prevents lost updates using version-based concurrency control:

```java
@Entity
public class AccountEntity {
    @Version
    private Long version;

    // JPA automatically checks version on update
}
```

**Process**:
1. Read entity with current version
2. Perform business logic
3. Update with version check
4. If version changed, throw exception and retry

#### Idempotency Protection
Prevents duplicate operations:

```java
// Check if operation already processed
if (idempotencyRepository.exists(idempotencyKey)) {
    return previousResult;
}

// Mark operation as processed
idempotencyRepository.save(new IdempotencyKey(key, operation));
```

**Configuration**:
- Default expiration: 24 hours
- Automatic cleanup via scheduled task
- Per-operation idempotency tracking

#### Transaction Safety
ACID compliance through Spring transactions:

```java
@Transactional
public DepositResponse deposit(DepositCommand command) {
    // All-or-nothing transaction
    // Automatic rollback on exception
}
```

### Input Validation

Multiple layers of validation ensure data integrity:

1. **DTO Validation** (Infrastructure Layer)
   ```java
   @NotNull(message = "Amount is required")
   @DecimalMin(value = "0.01", message = "Amount must be positive")
   private BigDecimal amount;
   ```

2. **Domain Validation** (Domain Layer)
   ```java
   public Money(BigDecimal amount) {
       if (amount.compareTo(BigDecimal.ZERO) < 0) {
           throw new IllegalArgumentException("Amount cannot be negative");
       }
   }
   ```

3. **Database Constraints** (Infrastructure Layer)
   ```java
   @Column(nullable = false, unique = true)
   private String emailHash;
   ```

### Error Handling

#### RFC 7807 Compliant Error Responses

All errors follow the Problem Details for HTTP APIs standard:

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

**Features**:
- Standardized error format
- No sensitive data leakage
- Correlation with trace IDs
- HTTP status code alignment
- Additional context for debugging

#### Domain Exception Hierarchy

```java
DomainException (base)
├── InsufficientFundsException
├── AccountNotFoundException
├── DuplicateEmailException
├── IdempotencyException
├── InvalidAccountStateException
└── ConcurrentModificationException
```

Each exception provides:
- Business-meaningful error messages
- Appropriate HTTP status codes
- Additional context for problem resolution

## Testing

### Unit Testing Strategy

Test domain logic in isolation:

```java
@Test
void shouldDepositMoney() {
    Account account = new Account(...);
    Money depositAmount = new Money(new BigDecimal("100.00"));

    account.deposit(depositAmount, "Test deposit");

    assertThat(account.getBalance()).isEqualTo(depositAmount);
}
```

### Integration Testing

Test complete use cases with test containers:

```java
@SpringBootTest
@Testcontainers
class DepositServiceIntegrationTest {
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Test
    void shouldDepositMoneyEndToEnd() {
        // Test complete flow from controller to database
    }
}
```

### API Testing Examples

#### Create Account (V2)
```bash
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
```

**Expected Response**: `201 Created`
```json
{
  "account_number": "1234567890",
  "holder_name": "Test User",
  "email_address": "test@example.com",
  "balance": 100.00,
  "account_type": "CHECKING",
  "status": "ACTIVE"
}
```

#### Get Account Details
```bash
curl -X GET http://localhost:8080/api/v2/accounts/1234567890 \
  -H "Accept: application/json"
```

#### Deposit with Idempotency
```bash
curl -X POST http://localhost:8080/api/v2/accounts/1234567890/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 250.00,
    "description": "Test deposit",
    "idempotency_key": "test-deposit-001",
    "transaction_reference": "TXN-001",
    "category": "SALARY"
  }'
```

**Idempotency Test**: Repeat the same request - should return the same result without creating duplicate transaction.

#### Withdraw Money
```bash
curl -X POST http://localhost:8080/api/v2/accounts/1234567890/withdraw \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50.00,
    "description": "ATM withdrawal",
    "idempotency_key": "test-withdraw-001"
  }'
```

#### Error Scenario - Insufficient Funds
```bash
curl -X POST http://localhost:8080/api/v2/accounts/1234567890/withdraw \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 999999.00,
    "description": "Large withdrawal",
    "idempotency_key": "test-large-withdraw"
  }'
```

**Expected Response**: `400 Bad Request` with RFC 7807 problem details

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests CreateAccountServiceTest

# Run with coverage report
./gradlew test jacocoTestReport

# Integration tests only
./gradlew integrationTest
```

## Configuration

### Application Properties

**Location**: `src/main/resources/application.properties`

#### Database Configuration
```properties
# MySQL Connection
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=banking_user
spring.datasource.password=secure_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

#### JPA/Hibernate Configuration
```properties
# Hibernate DDL
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Performance
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

#### Server Configuration
```properties
# Port
server.port=8080

# Error handling
server.error.include-message=always
server.error.include-binding-errors=always
server.error.include-stacktrace=never
server.error.include-exception=false
```

#### API Documentation
```properties
# Swagger/OpenAPI
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

#### Logging Configuration
```properties
# Logging levels
logging.level.root=INFO
logging.level.com.demo.banking_app=DEBUG
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Log file
logging.file.name=logs/banking-app.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### Environment Variables

Production deployments should use environment variables for sensitive configuration:

```bash
# Database credentials
export DB_USERNAME=banking_user
export DB_PASSWORD=secure_password_here
export DB_URL=jdbc:mysql://production-host:3306/banking_db

# Encryption keys (Base64 encoded)
export APP_CRYPTO_KEY=<32-byte-AES-key-base64-encoded>
export APP_CRYPTO_SALT=<HMAC-salt-base64-encoded>

# Server configuration
export SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=production

# Monitoring
export MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info,metrics
```

### Scheduled Tasks

#### Idempotency Cleanup
Automatically removes expired idempotency keys:

```java
@Scheduled(cron = "0 0 * * * *")  // Every hour
public void cleanupExpiredKeys() {
    idempotencyRepository.deleteExpiredKeys();
}
```

**Configuration**:
```properties
# Enable scheduling
spring.task.scheduling.enabled=true

# Thread pool size
spring.task.scheduling.pool.size=2
```

### Profile-based Configuration

#### Development Profile
```properties
# application-dev.properties
spring.jpa.show-sql=true
logging.level.com.demo.banking_app=DEBUG
```

#### Production Profile
```properties
# application-prod.properties
spring.jpa.show-sql=false
logging.level.com.demo.banking_app=INFO
server.error.include-stacktrace=never
```

**Activate profile**:
```bash
java -jar banking-app.jar --spring.profiles.active=prod
```

## Performance & Scalability

### Database Optimizations

#### Hash-based Indexing
Fast lookups on sensitive data using indexed hashes:

```sql
CREATE INDEX idx_account_number_hash ON accounts(account_number_hash);
CREATE INDEX idx_email_hash ON accounts(email_hash);
CREATE INDEX idx_phone_hash ON accounts(phone_number_hash);
```

**Benefits**:
- O(log n) lookup performance
- Privacy-preserving queries
- Efficient duplicate detection

#### Optimistic Locking Benefits
No database locks required for better concurrency:

```java
@Version
private Long version;  // Automatic version checking
```

**Advantages**:
- No lock contention
- Better concurrent throughput
- Scales horizontally
- Prevents lost updates

#### Connection Pooling
HikariCP for efficient connection management:

```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

#### Query Optimization
- Lazy loading for related entities
- Batch insert/update operations
- Indexed columns for frequent queries
- Prepared statements prevent SQL injection and improve performance

### Application Performance

#### Caching Strategy
Consider adding caching for read-heavy operations:

```java
@Cacheable(value = "accounts", key = "#accountNumber")
public Account findByAccountNumber(String accountNumber) {
    // Cached for improved read performance
}
```

#### Async Processing
Domain events enable asynchronous processing:

```java
@Async
public void publishEvent(DomainEvent event) {
    // Process event asynchronously
}
```

### Monitoring & Metrics

#### Structured Logging
JSON-formatted logs for analysis and aggregation:

```java
log.info("Account created",
    kv("accountNumber", accountNumber),
    kv("accountType", accountType),
    kv("traceId", traceId)
);
```

#### Application Metrics
Spring Boot Actuator provides key metrics:

- JVM memory and CPU usage
- HTTP request metrics
- Database connection pool stats
- Custom business metrics

**Endpoints**:
```bash
# Health check
GET /actuator/health

# Metrics
GET /actuator/metrics

# Info
GET /actuator/info
```

#### Trace IDs
Distributed tracing support for request correlation:

```java
@Component
public class TraceIdFilter implements Filter {
    // Inject trace ID into MDC
    MDC.put("traceId", generateTraceId());
}
```

### Scalability Considerations

#### Horizontal Scaling
Application is stateless and can scale horizontally:

- Deploy multiple instances behind load balancer
- No server-side session state
- Database handles concurrency with optimistic locking

#### Database Scaling
- Read replicas for query scaling
- Partitioning by account number hash
- Caching layer (Redis) for frequently accessed data

#### Event-Driven Architecture
Domain events enable:

- Asynchronous processing
- Service decoupling
- Event sourcing capability
- Integration with message brokers (Kafka, RabbitMQ)

## Account Types & Status

### Account Types

The system supports three types of bank accounts:

#### SAVINGS
Interest-bearing savings account

**Features**:
- Interest rate support
- Typically lower transaction frequency
- Higher interest rates
- Minimum balance requirements

**Use Cases**:
- Personal savings
- Emergency funds
- Goal-based savings

#### CHECKING
Transaction account for daily banking

**Features**:
- High transaction frequency
- Lower or no interest
- Debit card support
- Check writing capability

**Use Cases**:
- Daily transactions
- Bill payments
- Salary deposits

#### BUSINESS
Business account with enhanced features

**Features**:
- Higher transaction limits
- Multi-user access support
- Business reporting
- Integration capabilities

**Use Cases**:
- Business operations
- Payroll management
- Vendor payments

### Account Status

Account lifecycle is managed through status:

#### ACTIVE
Account can perform all transactions

**Allowed Operations**:
- Deposits
- Withdrawals
- Queries
- Updates

#### INACTIVE
Account is closed and disabled

**Allowed Operations**:
- Read-only queries (historical data)

**Restrictions**:
- No transactions allowed
- Cannot be reactivated

#### SUSPENDED
Account temporarily suspended

**Allowed Operations**:
- Read-only queries

**Restrictions**:
- No deposits or withdrawals
- Can be reactivated to ACTIVE

**Common Reasons**:
- Fraud investigation
- Compliance review
- Customer request

### Status Transitions

```
         ┌─────────┐
         │  NEW    │
         └────┬────┘
              │
              ▼
         ┌─────────┐
    ┌───│ ACTIVE  │───┐
    │   └─────────┘   │
    │                 │
    ▼                 ▼
┌──────────┐    ┌──────────┐
│SUSPENDED │    │ INACTIVE │
└────┬─────┘    └──────────┘
     │
     └────────────────┘
      (Can reactivate)
```

## Recent Updates & Roadmap

### Recent Implementation (2024)

#### Completed Features
- **Complete Hexagonal Architecture** - Full migration to ports and adapters pattern
- **Domain-Driven Design** - Rich domain models with encapsulated business logic
- **Security Hardening** - Hash-based data protection and optional AES encryption
- **API Versioning** - V1 deprecation strategy with enhanced V2 API
- **Event-Driven Architecture** - Domain events for system integration
- **Production Readiness** - Comprehensive error handling, logging, and monitoring
- **Concurrency Safety** - Optimistic locking and idempotency support
- **Code Quality** - Clean architecture with separation of concerns

### Architecture Benefits

#### Testability
- Business logic isolated in domain layer
- Easy to unit test without infrastructure
- Mock-free domain testing
- Integration tests with test containers

#### Maintainability
- Clear boundaries between layers
- Single Responsibility Principle
- Explicit dependencies through ports
- Self-documenting code structure

#### Flexibility
- Easy to swap database implementations
- Framework-agnostic domain layer
- Plug-and-play adapters
- Technology migration without business logic changes

#### Scalability
- Stateless application design
- Horizontal scaling capability
- Event-driven integration
- Database scaling strategies

#### Security
- Multiple layers of data protection
- Hash-based privacy preservation
- Encryption support
- Comprehensive validation

### Future Enhancements

#### Planned Features
- **Event Sourcing** - Complete audit trail with event store
- **CQRS Pattern** - Separate read and write models for scalability
- **Message Broker Integration** - Kafka or RabbitMQ for event publishing
- **Advanced Caching** - Redis integration for improved performance
- **Account Transfers** - Inter-account transfer functionality
- **Transaction History** - Detailed transaction audit trail
- **Reporting** - Account statements and analytics
- **Microservices** - Extract bounded contexts into separate services

#### Performance Improvements
- Read replicas for query scaling
- Database partitioning strategies
- Advanced caching layer
- Query optimization and indexing

#### Security Enhancements
- OAuth2/JWT authentication
- Role-based access control (RBAC)
- Audit logging compliance
- Advanced fraud detection

## Contributing

### Development Workflow

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes following the architecture patterns
4. Write tests for new functionality
5. Ensure all tests pass (`./gradlew test`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

### Code Standards

- Follow hexagonal architecture principles
- Keep domain layer pure (no framework dependencies)
- Write comprehensive tests (unit + integration)
- Use meaningful variable and method names
- Follow Java naming conventions
- Add JavaDoc for public APIs
- Keep methods small and focused

### Testing Requirements

- Unit tests for all business logic
- Integration tests for use cases
- API tests for controllers
- Minimum 80% code coverage

## License

This project is licensed under the MIT License.

## Contact & Support

For questions, issues, or contributions:

- **GitHub Issues**: [Report bugs or request features](https://github.com/yourusername/stabex-app/issues)
- **Documentation**: See inline JavaDoc and this README
- **Architecture Guide**: See [API_VERSIONING_GUIDE.md](API_VERSIONING_GUIDE.md)

---

**Built with Spring Boot 3, Hibernate 6, MySQL 8, Hexagonal Architecture, Domain-Driven Design, and enterprise-grade security practices.**

**Tech Stack**: Java 17 | Spring Boot 3 | Spring Data JPA | Hibernate 6 | MySQL 8 | Gradle | OpenAPI/Swagger | HikariCP | SLF4J
