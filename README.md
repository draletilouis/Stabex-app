# Stabex Banking API

Production-ready Spring Boot banking API built with **Clean Architecture** principles, featuring MySQL integration, comprehensive request validation, versioned endpoints (v1 & v2), database constraints, optimistic locking, idempotency support, and **fully tested APIs**.

## 🏗️ Architecture

This application follows **Clean Architecture** and **Domain-Driven Design** principles:

- **Domain Layer**: Core business logic, entities, value objects, and domain events
- **Application Layer**: Use cases, services, and ports (interfaces)
- **Infrastructure Layer**: Database persistence, web controllers, and external integrations
- **Separation of Concerns**: Clear boundaries between layers with dependency inversion

## ✨ Features

- ✅ **Clean Architecture** with proper separation of concerns
- ✅ **Domain-Driven Design** with value objects and domain events
- ✅ **Versioned API**: v1 (classic) and v2 (enhanced response schema)
- ✅ **Comprehensive API Testing** - All endpoints validated and working
- ✅ Create accounts, fetch by number, deposit/withdraw operations
- ✅ Bean Validation (Jakarta) on DTOs and parameters
- ✅ Global exception handling with structured JSON errors
- ✅ **Database constraints via Flyway migrations**
- ✅ **Optimistic locking with @Version for concurrent updates**
- ✅ **Idempotency support for transactions**
- ✅ **Automatic cleanup of expired idempotency keys**
- ✅ **Domain Events** for account lifecycle management
- ✅ **Scheduled Tasks** for maintenance operations

## Prerequisites

- Java 17+
- MySQL 8.0+
- Gradle (wrapper included)

## Database Setup

1) Create database:
```sql
CREATE DATABASE banking_db;
```

2) Update credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3) **Database migrations are managed by Flyway** - schema will be automatically created and versioned on startup.

### Database Features
- **Unique constraints** on email and account numbers via database indexes
- **Optimistic locking** with version fields to prevent concurrent modification conflicts
- **Idempotency tracking** to handle duplicate requests safely
- **Automatic cleanup** of expired idempotency keys

## Running the Application

1) Clone and open the project

2) Crypto config (required for encryption):
```properties
# In application.properties there are secure dev defaults, but in prod set:
APP_CRYPTO_KEY=<Base64 32 bytes>
APP_CRYPTO_SALT=<Base64 32 bytes>
```
Generate keys (PowerShell):
```powershell
$b=New-Object 'System.Byte[]' 32; (New-Object System.Security.Cryptography.RNGCryptoServiceProvider).GetBytes($b); [Convert]::ToBase64String($b)
```

3) Run (choose a port):
```bash
./gradlew bootRun --args="--server.port=8081"
```
App will be on `http://localhost:8081` (or your chosen port).

## 🚀 API Endpoints

**All APIs are fully tested and validated!** Two versions available: v1 (camelCase) and v2 (snake_case with enhanced features).

### V1 API (Base: `/api/v1/accounts`)

#### Create Account
```http
POST /api/v1/accounts
Content-Type: application/json

{
  "accountHolderName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "accountType": "CHECKING",
  "initialBalance": 1000.00
}
```

#### Get Account Details
```http
GET /api/v1/accounts/{accountNumber}
```

#### Deposit Money
```http
POST /api/v1/accounts/{accountNumber}/deposit
Content-Type: application/json

{
  "amount": 250.00,
  "description": "Test deposit"
}
```

#### Withdraw Money
```http
POST /api/v1/accounts/{accountNumber}/withdraw
Content-Type: application/json

{
  "amount": 50.00,
  "description": "Test withdrawal"
}
```

### V2 API (Base: `/api/v2/accounts`)

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

#### Get Account Details
```http
GET /api/v2/accounts/{accountNumber}
```

#### Deposit Money (Enhanced)
```http
POST /api/v2/accounts/{accountNumber}/deposit
Content-Type: application/json

{
  "amount": 150.00,
  "description": "V2 test deposit",
  "idempotency_key": "test-deposit-001"
}
```

#### Withdraw Money (Enhanced)
```http
POST /api/v2/accounts/{accountNumber}/withdraw
Content-Type: application/json

{
  "amount": 75.00,
  "description": "V2 test withdrawal",
  "idempotency_key": "test-withdraw-001"
}
```

## Account Types
- `SAVINGS`
- `CHECKING`
- `BUSINESS`

## Account Status
- `ACTIVE`
- `INACTIVE`
- `SUSPENDED`

## Validation & Error Handling

Bean Validation (annotations on DTOs/params) plus global handlers provide clear messages.

- **AccountNotFoundException**: When account doesn't exist
- **InsufficientFundsException**: When withdrawal amount exceeds balance
- **InvalidAmountException**: When amount is zero or negative
- **AccountAlreadyExistsException**: When trying to create duplicate account
- **InactiveAccountException**: When performing operations on inactive account
- **OptimisticLockingException**: When concurrent modification conflicts occur
- **IdempotencyException**: When duplicate requests are detected
- **Validation errors**: 400 with field/parameter messages
- **Data integrity**: 409/400 with constraint details

## Response Examples

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

### Error Response
```json
{
    "timestamp": "2024-01-15T10:30:00",
    "status": 400,
    "error": "Insufficient Funds",
    "message": "Insufficient funds. Current balance: 100.00, Requested amount: 500.00",
    "details": {
        "currentBalance": 100.00,
        "requestedAmount": 500.00
    }
}
```

## 🧪 API Testing

**All APIs have been comprehensively tested and validated!** The application includes:

### ✅ Tested Features
- **Account Creation**: Both V1 and V2 APIs tested with various account types
- **Account Retrieval**: Successfully fetching account details by account number
- **Deposit Operations**: Money deposits with balance updates verified
- **Withdrawal Operations**: Money withdrawals with balance validation
- **Error Handling**: Proper error responses for invalid requests
- **Idempotency**: V2 API idempotency key support tested
- **Database Persistence**: All operations properly persisted to MySQL

### Test Examples

#### V1 API Testing
```bash
# Create Account
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountHolderName":"Test User","email":"test@example.com","phoneNumber":"+1234567890","accountType":"CHECKING","initialBalance":100.00}'

# Get Account
curl -X GET http://localhost:8080/api/v1/accounts/{accountNumber}

# Deposit
curl -X POST http://localhost:8080/api/v1/accounts/{accountNumber}/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount":250.00,"description":"Test deposit"}'

# Withdraw
curl -X POST http://localhost:8080/api/v1/accounts/{accountNumber}/withdraw \
  -H "Content-Type: application/json" \
  -d '{"amount":50.00,"description":"Test withdrawal"}'
```

#### V2 API Testing
```bash
# Create Account (Enhanced)
curl -X POST http://localhost:8080/api/v2/accounts \
  -H "Content-Type: application/json" \
  -d '{"holder_name":"V2 Test User","email_address":"v2test@example.com","phone":"+15550002222","account_type":"SAVINGS","account_holder_id":"ID001","branch_code":"BR001","currency":"USD","initial_deposit":200.00,"interest_rate":2.5}'

# Deposit with Idempotency
curl -X POST http://localhost:8080/api/v2/accounts/{accountNumber}/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount":150.00,"description":"V2 test deposit","idempotency_key":"test-deposit-001"}'
```

## Advanced Features

### Database Migrations (Flyway)
- **V1**: Creates accounts table with unique constraints and optimistic locking
- **V2**: Creates idempotency tracking table
- **V3**: Optional sample data (commented out)
- Automatic schema versioning and validation on startup

### Optimistic Locking
- **@Version** field on Account entity prevents concurrent modification conflicts
- Automatic retry mechanism through idempotency
- HTTP 409 (Conflict) responses for concurrent modification attempts

### Idempotency Support
- **Required idempotency key** for all transaction requests
- **Duplicate request detection** - returns cached response for completed operations
- **Automatic cleanup** of expired idempotency keys (24-hour expiration)
- **Scheduled maintenance** runs every hour to clean up expired records

### Error Handling for Advanced Features
```json
// Optimistic Locking Failure
{
    "type": "about:blank",
    "title": "Concurrent Modification",
    "status": 409,
    "detail": "Account was modified by another transaction. Please retry.",
    "errorCode": "OPTIMISTIC_LOCKING_FAILURE"
}

// Idempotency Violation
{
    "type": "about:blank",
    "title": "Idempotency Violation",
    "status": 409,
    "detail": "Previous request with this idempotency key failed: Insufficient funds",
    "errorCode": "IDEMPOTENCY_VIOLATION"
}
```

## 🏛️ Clean Architecture Implementation

The application follows **Clean Architecture** principles with clear separation of concerns:

### Domain Layer (`domain/`)
- **Entities**: `Account`, `IdempotencyKey` with business rules
- **Value Objects**: `AccountId`, `AccountNumber`, `Email`, `PhoneNumber`, `Money`
- **Domain Events**: `AccountCreatedEvent`, `DepositCompletedEvent`
- **Exceptions**: Domain-specific exceptions with business context
- **Enums**: `AccountType`, `AccountStatus`, `OperationType`

### Application Layer (`application/`)
- **Use Cases**: `CreateAccountUseCase`, `DepositUseCase`, `WithdrawUseCase`, `GetAccountUseCase`
- **Services**: Application services implementing use cases
- **Commands/Responses**: DTOs for use case communication
- **Ports**: Interfaces defining contracts (inbound/outbound)

### Infrastructure Layer (`infrastructure/`)
- **Persistence**: JPA repositories, entities, mappers
- **Web Controllers**: REST endpoints for V1 and V2 APIs
- **Event Publishing**: Domain event publishing implementation
- **Utilities**: JSON utilities and helper classes

### Key Benefits
- **Testability**: Easy to unit test business logic in isolation
- **Maintainability**: Clear boundaries and single responsibility
- **Flexibility**: Easy to swap implementations (e.g., different databases)
- **Domain Focus**: Business logic is independent of external concerns

## Security & Privacy

- AES-GCM encryption at application layer for PII and account numbers
- Deterministic HMAC-SHA256 for equality queries (no plaintext search)
- **Database constraints** prevent duplicate accounts and data integrity violations
- **Optimistic locking** prevents race conditions and concurrent modification conflicts
- **Idempotency** ensures safe handling of duplicate requests
- Set keys via environment in production (do not commit secrets)
- Consider: authn/z, rate limiting, audit logging, secret rotation

## Testing Advanced Features

### Testing Idempotency
```bash
# First request
curl -X POST http://localhost:8081/api/v1/accounts/deposit \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"1234567890","amount":100.00,"description":"test","idempotencyKey":"test-key-001"}'

# Duplicate request (should return same response)
curl -X POST http://localhost:8081/api/v1/accounts/deposit \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"1234567890","amount":100.00,"description":"test","idempotencyKey":"test-key-001"}'
```

### Testing Concurrent Access
Send multiple simultaneous requests to the same account to test optimistic locking behavior.

## 🎯 Recent Updates

### Latest Changes (2025-09-19)
- ✅ **Complete Clean Architecture Refactoring**: Migrated from traditional layered architecture to Clean Architecture
- ✅ **Domain-Driven Design Implementation**: Added value objects, domain events, and proper domain modeling
- ✅ **Comprehensive API Testing**: All V1 and V2 endpoints tested and validated
- ✅ **Enhanced Error Handling**: Improved exception handling with domain-specific exceptions
- ✅ **Idempotency Support**: V2 API includes full idempotency key support
- ✅ **Database Optimization**: Improved persistence layer with proper mapping
- ✅ **Code Quality**: Better separation of concerns and maintainable code structure

### Testing Results
- **V1 APIs**: ✅ Account creation, retrieval, deposits, withdrawals - All working
- **V2 APIs**: ✅ Enhanced account creation, transactions with idempotency - All working
- **Database**: ✅ All operations properly persisted and retrieved
- **Error Handling**: ✅ Proper validation and error responses
- **Performance**: ✅ Application running smoothly with scheduled maintenance

---

**Built with Spring Boot 3, Hibernate 6, MySQL 8, Flyway, Clean Architecture, and enterprise-grade concurrency controls.**
