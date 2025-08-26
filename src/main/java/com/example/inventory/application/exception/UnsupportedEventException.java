package com.example.inventory.application.exception;

public class UnsupportedEventException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String event;
    private final String aggregateType;

    public UnsupportedEventException(String event, String aggregateType) {
        super("Evento no soportado [" + aggregateType + "]: " + event);
        this.event = event;
        this.aggregateType = aggregateType;
    }

    public String getEvent() {
        return event;
    }

    public String getAggregateType() {
        return aggregateType;
    }
}