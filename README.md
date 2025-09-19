# Stabex Banking API

Production-ready Spring Boot banking API with MySQL, request validation, application-layer encryption, versioned endpoints (v1 & v2), database constraints, optimistic locking, and idempotency support.

## Features

- ✅ Versioned API: v1 (classic) and v2 (enhanced response schema)
- ✅ Create accounts, fetch by number/id, list, search
- ✅ Deposit / Withdraw with validations and clear errors
- ✅ Update status; soft delete safety
- ✅ Bean Validation (Jakarta) on DTOs and parameters
- ✅ Global exception handling with structured JSON errors
- ✅ Application-layer encryption (AES-GCM) for PII and account numbers
- ✅ Deterministic hashing (HMAC-SHA256) for secure equality lookups
- ✅ **Database constraints via Flyway migrations**
- ✅ **Optimistic locking with @Version for concurrent updates**
- ✅ **Idempotency support for transfers/deposits**
- ✅ **Automatic cleanup of expired idempotency keys**

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

## API Endpoints

There are two versions: v1 (camelCase) and v2 (snake_case fields in responses and some requests).

### V1 (Base: `/api/v1/accounts`)
- Create (POST `/api/v1/accounts`)
```json
{
  "accountHolderName": "John Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+1234567890",
  "accountType": "SAVINGS"
}
```
- Get by number: `GET /api/v1/accounts/{accountNumber}`
- Get by id: `GET /api/v1/accounts/id/{id}`
- List: `GET /api/v1/accounts`
- Search: `GET /api/v1/accounts/search?name=John`
- Deposit (POST `/api/v1/accounts/deposit`)
```json
{
  "accountNumber": "1234567890",
  "amount": 100.00,
  "description": "deposit",
  "idempotencyKey": "deposit-1234567890-2024-01-15-001"
}
```
- Withdraw (POST `/api/v1/accounts/withdraw`) – same body as deposit
- Update status: `PUT /api/v1/accounts/{accountNumber}/status?status=INACTIVE`
- Delete: `DELETE /api/v1/accounts/{accountNumber}` (soft-delete semantics)

### V2 (Base: `/api/v2/accounts`)
- Create (POST `/api/v2/accounts`)
```json
{
  "holder_name": "Jane Roe",
  "email_address": "jane.roe@example.com",
  "phone": "+15550002222",
  "account_type": "CHECKING",
  "account_holder_id": "AH-0001",
  "branch_code": "001",
  "currency": "USD",
  "initial_deposit": 100.0
}
```
- Get by number: `GET /api/v2/accounts/{accountNumber}`
- Get by id: `GET /api/v2/accounts/id/{id}`
- List: `GET /api/v2/accounts`
- Search: `GET /api/v2/accounts/search?name=Bob`
- Balance: `GET /api/v2/accounts/{accountNumber}/balance`
- Deposit (POST `/api/v2/accounts/deposit`)
```json
{
  "account_number": "1234567890",
  "amount": 75.0,
  "description": "deposit",
  "reference": "REF-123",
  "idempotency_key": "deposit-1234567890-2024-01-15-001"
}
```
- Withdraw (POST `/api/v2/accounts/withdraw`) – same body shape as deposit
- Delete: `DELETE /api/v2/accounts/{accountNumber}`
- Health: `GET /api/v2/accounts/health`

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

## Testing the API

Use Postman or curl. Example (v1):
```bash
curl -X POST http://localhost:8081/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountHolderName":"John Doe","email":"john.doe@example.com","phoneNumber":"+1234567890","accountType":"SAVINGS"}'
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

## Architecture

Layered design:

- **Controller**: HTTP endpoints for v1/v2
- **Service**: Business logic, hashing, validations, idempotency handling
- **Repository**: JPA access using secure hash lookups
- **Entity/DTO**: Encrypted columns via JPA converters; versioned DTOs with idempotency
- **Exception**: Global handler for validation/integrity errors
- **Migration**: Flyway-managed database schema with constraints
- **Scheduling**: Automatic cleanup of expired idempotency keys

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

---

Made with Spring Boot 3, Hibernate 6, MySQL 8, Flyway, and enterprise-grade concurrency controls.
