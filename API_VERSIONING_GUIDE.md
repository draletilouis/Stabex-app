# API Versioning Implementation Guide

This document explains the comprehensive API versioning implementation for the Banking Application.

## Overview

The banking application now supports multiple API versioning strategies to ensure backward compatibility and smooth evolution of the API.

## Supported Versioning Strategies

### 1. URL Path Versioning (Primary)
- **Pattern**: `/api/v{version}/resource`
- **Examples**: 
  - `/api/v1/accounts` - Version 1 API
  - `/api/v2/accounts` - Version 2 API

### 2. Header-Based Versioning
- **Header**: `X-API-Version`
- **Example**: `X-API-Version: 2`

### 3. Content Negotiation Versioning
- **Accept Header**: `Accept: application/json;version=2`
- **Example**: `Accept: application/json;charset=utf-8;version=2.0`

## Version Priority

The system checks for API versions in the following order:
1. **X-API-Version header** (highest priority)
2. **URL path version**
3. **Accept header version**
4. **Default version** (v1)

## Supported Versions

- **v1**: Original API with basic functionality
- **v2**: Enhanced API with additional features and improved structure

## API v1 Endpoints

### Account Management
```
POST   /api/v1/accounts                    # Create account
GET    /api/v1/accounts/{accountNumber}    # Get account by number
GET    /api/v1/accounts/id/{id}            # Get account by ID
GET    /api/v1/accounts                    # Get all accounts
GET    /api/v1/accounts/search?name={name} # Search accounts
DELETE /api/v1/accounts/{accountNumber}    # Delete account
PUT    /api/v1/accounts/{accountNumber}/status?status={status} # Update status
```

### Transactions
```
POST   /api/v1/accounts/deposit            # Deposit funds
POST   /api/v1/accounts/withdraw           # Withdraw funds
```

### Health Check
```
GET    /api/v1/accounts/health             # Health check
```

## API v2 Endpoints

### Enhanced Account Management
```
POST   /api/v2/accounts                    # Create account (enhanced)
GET    /api/v2/accounts/{accountNumber}    # Get account by number (enhanced)
GET    /api/v2/accounts/id/{id}            # Get account by ID (enhanced)
GET    /api/v2/accounts                    # Get all accounts (enhanced)
GET    /api/v2/accounts/search?name={name} # Search accounts (enhanced)
DELETE /api/v2/accounts/{accountNumber}    # Delete account
PUT    /api/v2/accounts/{accountNumber}/status?status={status} # Update status
```

### Enhanced Transactions
```
POST   /api/v2/accounts/deposit            # Deposit funds (enhanced)
POST   /api/v2/accounts/withdraw           # Withdraw funds (enhanced)
```

### New v2 Features
```
GET    /api/v2/accounts/{accountNumber}/transactions # Get account transactions
GET    /api/v2/accounts/{accountNumber}/balance      # Get account balance
```

### Health Check
```
GET    /api/v2/accounts/health             # Health check (v2)
```

## Version-Specific DTOs

### v1 DTOs
- `AccountResponse`
- `CreateAccountRequest`
- `TransactionRequest`
- `TransactionResponse`

### v2 DTOs
- `AccountResponseV2` - Enhanced with additional fields
- `CreateAccountRequestV2` - Enhanced with validation and new fields
- `TransactionRequestV2` - Enhanced with additional transaction metadata
- `TransactionResponseV2` - Enhanced with transaction tracking

## Key Differences Between v1 and v2

### v1 Features
- Basic account management
- Simple transaction processing
- Standard JSON responses
- Basic validation

### v2 Enhancements
- **Enhanced DTOs**: More detailed response objects with additional metadata
- **Better Validation**: More comprehensive input validation
- **Transaction Tracking**: Enhanced transaction metadata and tracking
- **Additional Endpoints**: New endpoints for balance checking and transaction history
- **Improved Error Handling**: Better error responses and status codes
- **Metadata Support**: Support for categories, tags, and external references

## Example Usage

### Using URL Path Versioning
```bash
# v1 API
curl -X GET http://localhost:8080/api/v1/accounts/1234567890

# v2 API
curl -X GET http://localhost:8080/api/v2/accounts/1234567890
```

### Using Header Versioning
```bash
# v1 API
curl -X GET http://localhost:8080/api/accounts/1234567890 \
  -H "X-API-Version: 1"

# v2 API
curl -X GET http://localhost:8080/api/accounts/1234567890 \
  -H "X-API-Version: 2"
```

### Using Content Negotiation
```bash
# v1 API
curl -X GET http://localhost:8080/api/accounts/1234567890 \
  -H "Accept: application/json;version=1"

# v2 API
curl -X GET http://localhost:8080/api/accounts/1234567890 \
  -H "Accept: application/json;version=2"
```

## Configuration

### Application Properties
```properties
# API Versioning Configuration
api.versioning.enabled=true
api.versioning.default-version=1
api.versioning.supported-versions=1,2
api.versioning.header-name=X-API-Version
api.versioning.path-pattern=/api/v{version}/**

# Swagger Configuration
springdoc.paths-to-match=/api/v1/**,/api/v2/**
```

## Error Handling

### Unsupported Version Error
```json
{
  "error": "Unsupported API version",
  "version": "3",
  "supportedVersions": ["1", "2"]
}
```

### Version Detection
The system automatically detects the API version from:
1. Request headers
2. URL path
3. Accept header
4. Defaults to v1

## Implementation Details

### Core Components
- `ApiVersioningConfig` - Main configuration class
- `ApiVersionUtil` - Utility methods for version extraction
- `ApiVersionInterceptor` - Request interceptor for version validation
- `WebMvcConfig` - Web configuration with interceptor registration

### Controller Structure
- `AccountController` - v1 API controller
- `AccountControllerV2` - v2 API controller with enhanced features

### DTO Structure
- `dto/` - v1 DTOs
- `dto/v2/` - v2 DTOs with enhanced features

## Migration Guide

### From v1 to v2
1. **Update API endpoints**: Change `/api/v1/` to `/api/v2/`
2. **Update DTOs**: Use v2 DTOs for enhanced features
3. **Add new fields**: Utilize additional metadata fields
4. **Enhanced validation**: Leverage improved validation rules

### Backward Compatibility
- v1 API remains fully functional
- v2 API is additive, not breaking
- Both versions can coexist
- Gradual migration is supported

## Best Practices

1. **Always specify version**: Use explicit versioning in requests
2. **Handle version errors**: Implement proper error handling for unsupported versions
3. **Document changes**: Clearly document differences between versions
4. **Test both versions**: Ensure both v1 and v2 work correctly
5. **Plan deprecation**: Have a plan for eventually deprecating older versions

## Future Enhancements

- **v3 API**: Planned for future with additional features
- **Deprecation warnings**: Add deprecation headers for older versions
- **Version negotiation**: Automatic version negotiation based on client capabilities
- **Analytics**: Track API version usage for better insights
