# API Versioning Implementation Guide

This document explains the API versioning implementation for the Banking Application.

## Overview

The banking application now supports multiple API versioning strategies to ensure backward compatibility and smooth evolution of the API.

## Versioning Strategy

### URL Path Versioning (Implemented)
- **Pattern**: `/api/v{version}/resource`
- **Examples**: 
  - `/api/v1/accounts` - Version 1 API
  - `/api/v2/accounts` - Version 2 API

## Version Priority

The system determines the API version from the request path. If no version is present, the endpoint is not matched.

## Supported Versions

- **v1**: Original API with basic functionality
- **v2**: Enhanced API with additional features and improved structure

## API v1 Endpoints

### Endpoints (based on controllers)
```
POST   /api/v1/accounts                          # Create account
POST   /api/v1/accounts/{accountNumber}/deposit  # Deposit funds
POST   /api/v1/accounts/{accountNumber}/withdraw # Withdraw funds
GET    /api/v1/accounts/{accountNumber}          # Get account by number
```

## API v2 Endpoints

### Endpoints (based on controllers)
```
POST   /api/v2/accounts                          # Create account (enhanced)
POST   /api/v2/accounts/{accountNumber}/deposit  # Deposit funds (enhanced)
POST   /api/v2/accounts/{accountNumber}/withdraw # Withdraw funds (enhanced)
GET    /api/v2/accounts/{accountNumber}          # Get account by number (enhanced)
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

### Notes
- Header-based and content-negotiation versioning are not implemented at this time.

## Configuration

### Application Properties
```properties
# Swagger Configuration
springdoc.paths-to-match=/api/v1/**,/api/v2/**
```

## Error Handling

### Version Detection
The system detects the version from the URL path only (e.g., `/api/v1/**`, `/api/v2/**`).

## Implementation Details

### Controller Structure
- `AccountControllerV1` - v1 API controller
- `AccountControllerV2` - v2 API controller

### DTO Structure
- `dto/` - v1 DTOs
- `dto/v2/` - v2 DTOs with enhanced features

## Migration Guide

### From v1 to v2
1. **Update API endpoints**: Change `/api/v1/` to `/api/v2/`
2. **Update DTOs**: Use v2 DTOs for enhanced features
3. **Add new fields**: Utilize additional metadata fields (where applicable)
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
