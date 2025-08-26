package com.example.inventory.infrastructure.messaging.dto;

import com.example.inventory.domain.event.EventCategory;

public class DomainEventMessage {
	private String origin;
	private String eventType;
	private String aggregateType;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getAggregateType() {
		return aggregateType;
	}

	public void setAggregateType(String aggregateType) {
		this.aggregateType = aggregateType;
	}

	public boolean isForStore(String name) {
		if (this.origin != null && this.origin.equals(name)) {
			return true;
		}
		return false;
	}
}
