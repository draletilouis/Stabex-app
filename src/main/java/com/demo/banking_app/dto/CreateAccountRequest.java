package com.demo.banking_app.dto;

import com.demo.banking_app.domain.model.AccountType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {
    
    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
    private String phoneNumber;
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
}
