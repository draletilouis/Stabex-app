# Banking Application

A simple Spring Boot banking application with MySQL integration that provides comprehensive account management functionality.

## Features

- ✅ Create bank accounts
- ✅ Fetch account details
- ✅ Make deposits
- ✅ Withdraw funds
- ✅ Delete accounts (soft delete)
- ✅ List all accounts
- ✅ Search accounts by holder name
- ✅ Optimal exception handling
- ✅ Input validation
- ✅ Transaction logging

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven/Gradle

## Database Setup

1. Install MySQL and create a database:
```sql
CREATE DATABASE banking_db;
```

2. Update the database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Account Management

#### Create Account
```http
POST /api/accounts
Content-Type: application/json

{
    "accountHolderName": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "accountType": "SAVINGS"
}
```

#### Get Account by Account Number
```http
GET /api/accounts/{accountNumber}
```

#### Get Account by ID
```http
GET /api/accounts/id/{id}
```

#### Get All Accounts
```http
GET /api/accounts
```

#### Search Accounts by Holder Name
```http
GET /api/accounts/search?name=John
```

#### Delete Account
```http
DELETE /api/accounts/{accountNumber}
```

#### Update Account Status
```http
PUT /api/accounts/{accountNumber}/status?status=INACTIVE
```

### Transactions

#### Deposit
```http
POST /api/accounts/deposit
Content-Type: application/json

{
    "accountNumber": "1234567890",
    "amount": 1000.00,
    "description": "Initial deposit"
}
```

#### Withdraw
```http
POST /api/accounts/withdraw
Content-Type: application/json

{
    "accountNumber": "1234567890",
    "amount": 500.00,
    "description": "ATM withdrawal"
}
```

### Health Check
```http
GET /api/accounts/health
```

## Account Types
- `SAVINGS`
- `CHECKING`
- `BUSINESS`

## Account Status
- `ACTIVE`
- `INACTIVE`
- `SUSPENDED`

## Exception Handling

The application includes comprehensive exception handling for:

- **AccountNotFoundException**: When account doesn't exist
- **InsufficientFundsException**: When withdrawal amount exceeds balance
- **InvalidAmountException**: When amount is zero or negative
- **AccountAlreadyExistsException**: When trying to create duplicate account
- **InactiveAccountException**: When performing operations on inactive account
- **ValidationException**: When input validation fails

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

You can test the API using tools like:
- Postman
- curl
- REST Client extensions

### Example curl commands:

Create an account:
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "John Doe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "accountType": "SAVINGS"
  }'
```

Make a deposit:
```bash
curl -X POST http://localhost:8080/api/accounts/deposit \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "1234567890",
    "amount": 1000.00,
    "description": "Initial deposit"
  }'
```

## Architecture

The application follows a layered architecture:

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Handles data access
- **Entity Layer**: JPA entities for database mapping
- **DTO Layer**: Data transfer objects for API communication
- **Exception Layer**: Custom exceptions and global exception handling

## Security Considerations

For production use, consider adding:
- Authentication and authorization
- Input sanitization
- Rate limiting
- Audit logging
- Encryption for sensitive data
