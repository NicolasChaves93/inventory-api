package com.example.inventory.domain.event;

import java.time.Instant;

import com.example.inventory.domain.model.Store;

public class StoreEventFactory {

    private StoreEventFactory() {} // No se puede instanciar

    /**
     * Crea un DomainEvent a partir de una tienda y un tipo de evento.
     *
     * @param store La tienda afectada
     * @param type Tipo de evento
     * @param origin Rol que genera el evento ("Central" o "Local")
     * @return DomainEvent listo para publicar
     */
    public static DomainEvent createEvent(Store store, StoreEventType type, String origin) {
        return new DomainEvent() {

            @Override
            public String eventType() {
                return type.name();
            }

            @Override
            public String aggregateType() {
                return EventCategory.STORE.name();
            }

            @Override
            public String aggregateId() {
                return store.getName();
            }

            @Override
            public Instant occurredOn() {
                return Instant.now();
            }

            @Override
            public String toJson() {
                return "{"
                        + "\"origin\":\"" + origin + "\","
                        + "\"eventType\":\"" + type.name() + "\","
                        + "\"aggregateType\":\"" + aggregateType() + "\","
                        + "\"name\":\"" + store.getName() + "\","
                        + "\"address\":\"" + store.getAddress() + "\""
                        + "}";
            }
        };
    }
}
