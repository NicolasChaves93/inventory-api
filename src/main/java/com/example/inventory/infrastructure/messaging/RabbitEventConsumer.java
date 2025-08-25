package com.example.inventory.infrastructure.messaging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.domain.event.InventoryEventType;
import com.example.inventory.domain.model.Inventory;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.domain.repository.StoreRepository;
import com.example.inventory.infrastructure.messaging.dto.InventoryEventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Consumidor RabbitMQ que aplica en la base de datos central
 * los eventos emitidos por la API.
 * 
 * Si ocurre un error de negocio o de parsing,
 * el mensaje es rechazado y redirigido a la DLQ automáticamente.
 */
@Component
public class RabbitEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitEventConsumer.class);

    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public RabbitEventConsumer(InventoryRepository inventoryRepository,
                               StoreRepository storeRepository,
                               ProductRepository productRepository,
                               ObjectMapper objectMapper) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${app.messaging.queue}")
    @Transactional
    public void onMessage(String payload) {
        log.info("Mensaje recibido de RabbitMQ: {}", payload);

        try {
            InventoryEventMessage message = objectMapper.readValue(payload, InventoryEventMessage.class);
            InventoryEventType type = InventoryEventType.valueOf(message.event());

            switch (type) {
                case INVENTORY_CREATED -> handleCreate(message);
                case INVENTORY_DELETED -> handleDelete(message);
                case STOCK_ADJUSTED      -> handleAdd(message);
                case STOCK_REMOVED    -> handleRemove(message);
                default -> throw new IllegalArgumentException("Evento no soportado: " + message.event());
            }

            log.info("Evento procesado correctamente: {}", message.event());

        } catch (IOException e) {
            log.error("Error deserializando evento: {}", payload, e);
            throw new RuntimeException("Error deserializando evento", e);
        } catch (Exception e) {
            log.error("Error procesando evento: {}", payload, e);
            // relanzar la excepción para que RabbitMQ lo mueva a la DLQ
            throw e;
        }
    }

    private void handleCreate(InventoryEventMessage message) {
        Store store = storeRepository.findByName(message.store())
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada: " + message.store()));
        Product product = productRepository.findByCode(message.product())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + message.product()));

        boolean exists = inventoryRepository.findByStoreNameAndProductCode(message.store(), message.product()).isPresent();
        if (exists) {
            throw new IllegalStateException("Inventario duplicado: " + message.store() + "-" + message.product());
        }

        Inventory inventory = new Inventory(store, product, message.quantity());
        inventoryRepository.save(inventory);
    }

    private void handleDelete(InventoryEventMessage message) {
        Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(message.store(), message.product())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado: " + message.store() + "-" + message.product()));

        inventoryRepository.delete(inventory);
    }

    private void handleAdd(InventoryEventMessage message) {
        Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(message.store(), message.product())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado: " + message.store() + "-" + message.product()));

        if (message.quantity() <= 0) {
            throw new IllegalArgumentException("Cantidad inválida para STOCK_ADDED");
        }

        inventory.addStock(message.quantity());
        inventoryRepository.save(inventory);
    }

    private void handleRemove(InventoryEventMessage message) {
        Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(message.store(), message.product())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado: " + message.store() + "-" + message.product()));

        if (message.quantity() <= 0) {
            throw new IllegalArgumentException("Cantidad inválida para STOCK_REMOVED");
        }
        if (inventory.getQuantity() < message.quantity()) {
            throw new IllegalStateException("Stock insuficiente para remover " + message.quantity());
        }

        inventory.removeStock(message.quantity());
        inventoryRepository.save(inventory);
    }
}