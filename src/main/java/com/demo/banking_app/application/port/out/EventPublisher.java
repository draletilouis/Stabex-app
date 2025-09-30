package com.demo.banking_app.application.port.out;

import com.demo.banking_app.domain.model.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}




