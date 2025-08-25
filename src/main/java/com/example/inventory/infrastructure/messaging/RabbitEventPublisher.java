package com.example.inventory.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.inventory.application.port.out.EventPublisherPort;
import com.example.inventory.domain.event.DomainEvent;

/** Publica DomainEvent serializados a JSON en el exchange configurado. */
@Component
public class RabbitEventPublisher implements EventPublisherPort {
  private final RabbitTemplate template;

  @Value("${app.messaging.exchange}")
  private String exchange;

  @Value("${app.messaging.routing-key}")
  private String routingKey;

  public RabbitEventPublisher(RabbitTemplate template) { this.template = template; }

  @Override
  public void publish(DomainEvent event) {
    template.convertAndSend(exchange, routingKey, event.toJson());
  }
}