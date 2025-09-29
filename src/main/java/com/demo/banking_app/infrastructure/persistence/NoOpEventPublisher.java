package com.demo.banking_app.infrastructure.persistence;

import com.demo.banking_app.application.port.out.EventPublisher;
import com.demo.banking_app.domain.model.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NoOpEventPublisher implements EventPublisher {
    
    @Override
    public void publish(DomainEvent event) {
        log.info("Publishing domain event: {} with ID: {}", event.getClass().getSimpleName(), event.getEventId());
        // In a real implementation, this would publish to a message broker
        // For now, we just log the event
    }
}



