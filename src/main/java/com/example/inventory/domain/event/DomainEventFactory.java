package com.example.inventory.domain.event;

import java.time.Instant;

/**
 * Fábrica para crear eventos de dominio.
 */
public class DomainEventFactory {

    private DomainEventFactory() {
    }

    public static DomainEvent create(
            String eventType,
            String aggregateType,
            String aggregateId,
            String payloadJson
    ) {
        return new DomainEvent() {
            @Override
            public String eventType() { return eventType; }

            @Override
            public String aggregateType() { return aggregateType; }

            @Override
            public String aggregateId() { return aggregateId; }

            @Override
            public Instant occurredOn() { return Instant.now(); }

            @Override
            public String toJson() { return payloadJson; }
        };
    }
}