package com.example.inventory.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * Entidad técnica Outbox para publicación confiable de eventos.
 */
@Entity
@Table(name = "event_outbox")
public class OutboxEntity {

  @Id
  private String id;

  @Column(name = "aggregate_type", nullable = false)
  private String aggregateType;

  @Column(name = "event_type", nullable = false)
  private String eventType;

  @Lob
  @Column(name = "payload", nullable = false)
  private String payload;

  @Column(name = "occurred_on", nullable = false)
  private java.time.Instant occurredOn;

  @Column(name = "published", nullable = false)
  private Integer published = 0;
  
  // Getters y Setters

  public String getId() {
	return id;
  }

  public void setId(String id) {
	this.id = id;
  }

  public String getAggregateType() {
	return aggregateType;
  }

  public void setAggregateType(String aggregateType) {
	this.aggregateType = aggregateType;
  }

  public String getEventType() {
	return eventType;
  }

  public void setEventType(String eventType) {
	this.eventType = eventType;
  }

  public String getPayload() {
	return payload;
  }

  public void setPayload(String payload) {
	this.payload = payload;
  }

  public java.time.Instant getOccurredOn() {
	return occurredOn;
  }

  public void setOccurredOn(java.time.Instant occurredOn) {
	this.occurredOn = occurredOn;
  }

  public Integer getPublished() {
	return published;
  }

  public void setPublished(Integer published) {
	this.published = published;
  }
  
}