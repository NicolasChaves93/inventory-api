package com.example.inventory.domain.event;

import java.time.Instant;

/**
 * Interfaz base para eventos de dominio.
 */
public interface DomainEvent {
	String eventType();
	String aggregateType();
	String aggregateId();
	Instant occurredOn();
	String toJson();

}
