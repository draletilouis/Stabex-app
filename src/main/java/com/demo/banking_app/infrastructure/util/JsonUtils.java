package com.demo.banking_app.infrastructure.util;

import com.demo.banking_app.application.service.DepositResponse;
import com.demo.banking_app.application.service.WithdrawResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
    
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    
    public static String serializeDepositResponse(DepositResponse response) {
        // Return a simple string representation instead of JSON to avoid MySQL JSON validation issues
        return String.format("DepositResponse{accountNumber='%s', transactionType='%s', amount='%s', newBalance='%s', description='%s', idempotencyKey='%s', timestamp='%s'}",
                response.getAccountNumber(),
                response.getTransactionType(),
                response.getAmount(),
                response.getNewBalance(),
                response.getDescription() != null ? response.getDescription() : "",
                response.getIdempotencyKey(),
                response.getTimestamp().toString());
    }
    
    private static String escapeJsonString(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    public static String serializeWithdrawResponse(WithdrawResponse response) {
        // Return a simple string representation instead of JSON to avoid MySQL JSON validation issues
        return String.format("WithdrawResponse{accountNumber='%s', transactionType='%s', amount='%s', newBalance='%s', description='%s', idempotencyKey='%s', timestamp='%s'}",
                response.getAccountNumber(),
                response.getTransactionType(),
                response.getAmount(),
                response.getNewBalance(),
                response.getDescription() != null ? response.getDescription() : "",
                response.getIdempotencyKey(),
                response.getTimestamp().toString());
    }
    
    public static DepositResponse parseDepositResponse(String json) {
        try {
            return objectMapper.readValue(json, DepositResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse deposit response JSON: {}", json, e);
            throw new RuntimeException("Failed to parse cached response", e);
        }
    }
    
    public static WithdrawResponse parseWithdrawResponse(String json) {
        try {
            return objectMapper.readValue(json, WithdrawResponse.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse withdraw response JSON: {}", json, e);
            throw new RuntimeException("Failed to parse cached response", e);
        }
    }
}

