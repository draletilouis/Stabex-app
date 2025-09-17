package com.demo.banking_app.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice(basePackages = "com.demo.banking_app.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.NOT_FOUND, "Account Not Found", ex.getMessage(), "ACCOUNT_NOT_FOUND", request);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ProblemDetail handleInsufficientFundsException(InsufficientFundsException ex, HttpServletRequest request) {
        ProblemDetail pd = createProblemDetail(HttpStatus.BAD_REQUEST, "Insufficient Funds", ex.getMessage(), "INSUFFICIENT_FUNDS", request);
        pd.setProperty("currentBalance", ex.getCurrentBalance());
        pd.setProperty("requestedAmount", ex.getRequestedAmount());
        return pd;
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ProblemDetail handleInvalidAmountException(InvalidAmountException ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Invalid Amount", ex.getMessage(), "INVALID_AMOUNT", request);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ProblemDetail handleAccountAlreadyExistsException(AccountAlreadyExistsException ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.CONFLICT, "Account Already Exists", ex.getMessage(), "ACCOUNT_ALREADY_EXISTS", request);
    }

    @ExceptionHandler(InactiveAccountException.class)
    public ProblemDetail handleInactiveAccountException(InactiveAccountException ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Inactive Account", ex.getMessage(), "INACTIVE_ACCOUNT", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ProblemDetail pd = createProblemDetail(HttpStatus.BAD_REQUEST, "Validation Failed", "Invalid input data", "VALIDATION_FAILED", request);
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            String path = cv.getPropertyPath() != null ? cv.getPropertyPath().toString() : "parameter";
            errors.put(path, cv.getMessage());
        });

        ProblemDetail pd = createProblemDetail(HttpStatus.BAD_REQUEST, "Constraint Violation", "Invalid request parameters", "CONSTRAINT_VIOLATION", request);
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred", "INTERNAL_SERVER_ERROR", request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        String rootMessage = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        return createProblemDetail(HttpStatus.BAD_REQUEST, "Data Integrity Violation", rootMessage, "DATA_INTEGRITY_VIOLATION", request);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String errorCode, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        String requestPath = request.getRequestURI() != null ? request.getRequestURI() : "/";
        problemDetail.setInstance(URI.create(requestPath));
        problemDetail.setProperty("errorCode", errorCode);
        problemDetail.setProperty("path", requestPath);
        problemDetail.setProperty("traceId", resolveTraceId(request));
        return problemDetail;
    }

    private String resolveTraceId(HttpServletRequest request) {
        String mdcTraceId = MDC.get("traceId");
        if (mdcTraceId != null && !mdcTraceId.isBlank()) {
            return mdcTraceId;
        }
        String headerTraceId = request.getHeader("X-Request-Id");
        if (headerTraceId != null && !headerTraceId.isBlank()) {
            return headerTraceId;
        }
        return UUID.randomUUID().toString();
    }
}
