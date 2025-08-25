package com.example.inventory.infrastructure.messaging.dto;

/**
 * DTO genérico para deserializar eventos de inventario recibidos por RabbitMQ.
 */
public record InventoryEventMessage(
        String store,
        String product,
        int quantity,
        String event
) {}
