package com.demo.banking_app.application.port.in;

import com.demo.banking_app.application.service.CreateAccountCommand;
import com.demo.banking_app.application.service.CreateAccountResponse;

public interface CreateAccountUseCase {
    CreateAccountResponse createAccount(CreateAccountCommand command);
}

