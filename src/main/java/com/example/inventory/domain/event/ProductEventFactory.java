package com.example.inventory.domain.event;

import java.time.Instant;

import com.example.inventory.domain.model.Product;

public class ProductEventFactory {

    public static DomainEvent createEvent(Product product, ProductEventType type, String origin) {
        return new DomainEvent() {
            @Override
            public String eventType() {
                return type.name();
            }

            @Override
            public String aggregateType() {
                return EventCategory.PRODUCT.name();
            }

            @Override
            public String aggregateId() {
                return product.getCode();
            }

            @Override
            public java.time.Instant occurredOn() {
                return Instant.now();
            }

            @Override
            public String toJson() {
                return String.format(
                    "{\"origin\":\"%s\",\"eventType\":\"%s\",\"aggregateType\":\"%s\",\"code\":\"%s\",\"name\":\"%s\",\"description\":\"%s\",\"price\":%s}",
                    origin, type.name(), aggregateType(), product.getCode(), product.getName(), product.getDescription(), product.getPrice()
                );
            }

        };
    }
}