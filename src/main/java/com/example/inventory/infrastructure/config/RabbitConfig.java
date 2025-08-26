package com.example.inventory.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ con soporte para Dead Letter Queue (DLQ).
 * 
 * - La API publica eventos en el exchange principal.
 * - Los mensajes se enrutan a la cola principal.
 * - Si ocurre un error en el consumidor, el mensaje se mueve a la DLQ automáticamente.
 */
@Configuration
public class RabbitConfig {

  @Value("${app.messaging.exchange}")
  private String exchangeName;

  @Value("${app.messaging.queue}")
  private String baseQueueName;

  @Value("${store.name}")
  private String storeName;

  private String fullQueueName() {
    return baseQueueName + "." + storeName;
  }

  private String dlqName() {
    return fullQueueName() + ".dlq";
  }

  @Bean
  public TopicExchange inventoryExchange() {
    return new TopicExchange(exchangeName, true, false);
  }

  @Bean
  public Queue inventoryQueue() {
    return QueueBuilder.durable(fullQueueName())
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", dlqName())
        .build();
  }

  @Bean
  public Queue deadLetterQueue() {
    return QueueBuilder.durable(dlqName()).build();
  }

  // ya no se usa routingKey único -> agregamos bindings específicos
  @Bean
  public Binding bindingAll(Queue inventoryQueue, TopicExchange inventoryExchange,
                            @Value("${app.messaging.routing-key-all}") String routingKeyAll) {
    return BindingBuilder.bind(inventoryQueue).to(inventoryExchange).with(routingKeyAll);
  }

  @Bean
  public Binding bindingLocal(Queue inventoryQueue, TopicExchange inventoryExchange,
                              @Value("${app.messaging.routing-key-local}") String routingKeyLocal) {
    return BindingBuilder.bind(inventoryQueue).to(inventoryExchange).with(routingKeyLocal + "." + storeName);
  }
}