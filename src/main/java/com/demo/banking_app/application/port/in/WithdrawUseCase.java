package com.demo.banking_app.application.port.in;

import com.demo.banking_app.application.service.WithdrawCommand;
import com.demo.banking_app.application.service.WithdrawResponse;

public interface WithdrawUseCase {
    WithdrawResponse withdraw(WithdrawCommand command);
}

