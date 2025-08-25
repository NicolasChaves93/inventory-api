package com.example.inventory.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
  private String queueName;

  @Value("${app.messaging.routing-key}")
  private String routingKey;

  // Nombre de la DLQ (derivado del nombre de la cola principal)
  private String dlqName() {
    return queueName + ".dlq";
  }

  @Bean
  public TopicExchange inventoryExchange() {
    return new TopicExchange(exchangeName, true, false);
  }

  /**
   * Cola principal con referencia a la DLQ
   */
  @Bean
  public Queue inventoryQueue() {
    return QueueBuilder.durable(queueName)
        .withArgument("x-dead-letter-exchange", "") // exchange por defecto
        .withArgument("x-dead-letter-routing-key", dlqName()) // redirige a la DLQ
        .build();
  }

  /**
   * Dead Letter Queue para almacenar mensajes fallidos.
   */
  @Bean
  public Queue deadLetterQueue() {
    return QueueBuilder.durable(dlqName()).build();
  }

  @Bean
  public Binding inventoryBinding(Queue inventoryQueue, TopicExchange inventoryExchange) {
    return BindingBuilder.bind(inventoryQueue).to(inventoryExchange).with(routingKey);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    return new RabbitTemplate(connectionFactory);
  }
}