package com.demo.banking_app.application.port.in;

import com.demo.banking_app.application.service.DepositCommand;
import com.demo.banking_app.application.service.DepositResponse;

public interface DepositUseCase {
    DepositResponse deposit(DepositCommand command);
}

