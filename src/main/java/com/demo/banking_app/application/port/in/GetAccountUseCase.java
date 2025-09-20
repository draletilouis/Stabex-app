package com.demo.banking_app.application.port.in;

import com.demo.banking_app.application.service.GetAccountCommand;
import com.demo.banking_app.application.service.GetAccountResponse;

public interface GetAccountUseCase {
    GetAccountResponse getAccount(GetAccountCommand command);
}

