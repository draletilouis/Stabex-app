# Database and Domain Improvements Implementation

This document describes the implementation of database constraints, optimistic locking, and idempotency features for the banking application.

## Features Implemented

### 1. Database Constraints via Flyway

#### Flyway Integration
- Added Flyway dependencies to `build.gradle`
- Configured Flyway in `application.properties`
- Changed Hibernate DDL auto from `update` to `validate` for better control

#### Migration Scripts
- **V1__Create_accounts_table.sql**: Creates the accounts table with:
  - Unique constraints on `account_number_hash` and `email_hash`
  - Optimistic locking version field
  - Proper indexes for performance
- **V2__Create_idempotency_table.sql**: Creates the idempotency tracking table
- **V3__Add_sample_data.sql**: Optional sample data (commented out)

#### Database Constraints
```sql
-- Unique constraints for business logic
UNIQUE KEY uk_account_number_hash (account_number_hash),
UNIQUE KEY uk_email_hash (email_hash),

-- Indexes for performance
INDEX idx_account_holder_name (account_holder_name),
INDEX idx_status (status),
INDEX idx_created_at (created_at)
```

### 2. Optimistic Locking

#### Account Entity Updates
- Added `@Version` annotation to Account entity
- Version field automatically managed by JPA/Hibernate
- Prevents concurrent modification conflicts

#### Service Layer Updates
- Wrapped database operations in try-catch blocks
- Handle `ObjectOptimisticLockingFailureException`
- Throw custom `OptimisticLockingException` with meaningful messages
- Automatic retry mechanism through idempotency

#### Exception Handling
- Added `OptimisticLockingException` custom exception
- Updated `GlobalExceptionHandler` to handle optimistic locking failures
- Returns HTTP 409 (Conflict) status for concurrent modification attempts

### 3. Idempotency Implementation

#### IdempotencyKey Entity
- Tracks unique operation requests
- Stores operation type, account, amount, and status
- Includes expiration time for automatic cleanup
- JSON response data caching

#### IdempotencyService
- Creates and manages idempotency keys
- Handles duplicate request detection
- Caches successful responses
- Tracks failed operations
- Automatic cleanup of expired keys

#### DTO Updates
- Added `idempotencyKey` field to all transaction DTOs (v1 and v2)
- Required field validation
- Included in response objects

#### Service Integration
- Check for existing idempotency keys before processing
- Return cached responses for completed operations
- Fail fast for previously failed operations
- Mark operations as completed/failed with response data

#### Scheduled Cleanup
- Automatic cleanup of expired idempotency keys every hour
- Configurable expiration time (24 hours default)
- Prevents database bloat from old idempotency records

## Usage Examples

### Making an Idempotent Deposit Request

```json
POST /api/v1/accounts/deposit
{
  "accountNumber": "1234567890",
  "amount": 100.00,
  "description": "Salary deposit",
  "idempotencyKey": "deposit-1234567890-2024-01-15-001"
}
```

### Response for First Request
```json
{
  "accountNumber": "1234567890",
  "transactionType": "DEPOSIT",
  "amount": 100.00,
  "newBalance": 1100.00,
  "description": "Salary deposit",
  "idempotencyKey": "deposit-1234567890-2024-01-15-001",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Response for Duplicate Request (Same Idempotency Key)
Returns the same response as the first request, ensuring idempotency.

## Error Handling

### Optimistic Locking Failure
```json
{
  "type": "about:blank",
  "title": "Concurrent Modification",
  "status": 409,
  "detail": "Account was modified by another transaction. Please retry.",
  "errorCode": "OPTIMISTIC_LOCKING_FAILURE"
}
```

### Idempotency Violation
```json
{
  "type": "about:blank",
  "title": "Idempotency Violation",
  "status": 409,
  "detail": "Previous request with this idempotency key failed: Insufficient funds",
  "errorCode": "IDEMPOTENCY_VIOLATION"
}
```

## Configuration

### Application Properties
```properties
# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=validate
```

### Idempotency Settings
- Default expiration: 24 hours
- Cleanup frequency: Every hour
- Configurable via `IdempotencyService.IDEMPOTENCY_EXPIRY_HOURS`

## Benefits

1. **Data Integrity**: Unique constraints prevent duplicate accounts
2. **Concurrency Safety**: Optimistic locking prevents race conditions
3. **Idempotency**: Duplicate requests are handled safely
4. **Performance**: Proper indexing improves query performance
5. **Maintainability**: Flyway migrations provide version control for database schema
6. **Reliability**: Automatic cleanup prevents resource leaks

## Testing Recommendations

1. **Concurrent Access**: Test multiple simultaneous transactions on the same account
2. **Idempotency**: Verify duplicate requests return cached responses
3. **Optimistic Locking**: Test concurrent modifications trigger appropriate exceptions
4. **Cleanup**: Verify expired idempotency keys are removed automatically
5. **Database Constraints**: Test unique constraint violations are handled properly

## Production Considerations

1. **Monitoring**: Monitor optimistic locking failure rates
2. **Tuning**: Adjust idempotency expiration based on business requirements
3. **Scaling**: Consider distributed idempotency for multi-instance deployments
4. **Backup**: Ensure idempotency data is included in backup strategies
5. **Performance**: Monitor database performance with new constraints and indexes
