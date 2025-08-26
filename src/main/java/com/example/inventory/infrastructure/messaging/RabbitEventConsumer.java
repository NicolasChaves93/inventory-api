package com.example.inventory.infrastructure.messaging;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.domain.event.InventoryEventType;
import com.example.inventory.application.exception.UnsupportedEventException;
import com.example.inventory.application.port.out.EventPublisherPort;
import com.example.inventory.domain.model.Inventory;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.domain.repository.StoreRepository;
import com.example.inventory.infrastructure.messaging.dto.DomainEventMessage;
import com.example.inventory.infrastructure.messaging.dto.InventoryEventMessage;
import com.example.inventory.infrastructure.messaging.dto.StoreEventMessage;
import com.example.inventory.infrastructure.messaging.dto.ProductEventMessage;
import com.example.inventory.domain.event.ProductEventType;
import com.example.inventory.domain.event.StoreEventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.inventory.config.StoreProperties;

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
    private final StoreProperties storeProperties;

    public RabbitEventConsumer(InventoryRepository inventoryRepository,
            StoreRepository storeRepository,
            ProductRepository productRepository,
            ObjectMapper objectMapper,
            StoreProperties storeProperties,
            EventPublisherPort eventPublisher) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.storeProperties = storeProperties;
    }

    @RabbitListener(queues = "#{inventoryQueue.name}")
    @Transactional
    public void onMessage(String payload) {
        log.info("Mensaje recibido de RabbitMQ: {}", payload);
        try {
            // Deserializar polimórficamente
            DomainEventMessage baseMessage = objectMapper.readValue(payload, DomainEventMessage.class);

            // Ignorar mensaje propio si no es central
            if (!storeProperties.isCentral() && storeProperties.getRole().equalsIgnoreCase(baseMessage.getOrigin())) {
                log.info("Mensaje ignorado: origen propio {}", baseMessage.getOrigin());
                return;
            }

            processByCategory(payload, baseMessage);

        } catch (IOException e) {
            log.error("Error deserializando evento: {}", payload, e);
            throw new RuntimeException("Error deserializando evento", e);
        } catch (Exception e) {
            log.error("Error procesando evento: {}", payload, e);
            throw e;
        }
    }

    /**
     * Procesa el mensaje según su categoría, deserializando al tipo correcto.
     */
    private void processByCategory(String payload, DomainEventMessage baseMessage) throws IOException {
    	String type = baseMessage.getAggregateType();
        switch (type) {
            case "INVENTORY" -> {
                InventoryEventMessage inventoryEvent = objectMapper.readValue(payload, InventoryEventMessage.class);
                log.info("Procesando evento de inventario: {} - {}", inventoryEvent.getEventType(),
                        inventoryEvent.getOrigin());
                processInventory(inventoryEvent);
            }
            case "STORE" -> {
                StoreEventMessage storeEvent = objectMapper.readValue(payload, StoreEventMessage.class);
                log.info("Procesando evento de tienda: {} - {}", storeEvent.getEventType(), storeEvent.getOrigin());
                processStore(storeEvent);
            }
            case "PRODUCT" -> {
                ProductEventMessage productEvent = objectMapper.readValue(payload, ProductEventMessage.class);
                log.info("Procesando evento de producto: {} - {}", productEvent.getEventType(),
                        productEvent.getOrigin());
                processProduct(productEvent);
            }
            default ->
                throw new UnsupportedEventException(baseMessage.getEventType(), baseMessage.getAggregateType());
        }
    }

    // --- Procesadores ---
    private void processInventory(InventoryEventMessage message) {
        InventoryEventType type = InventoryEventType.valueOf(message.getEventType());
        switch (type) {
            case INVENTORY_CREATED -> {
                handleCreateInventory(message);
            }
            case INVENTORY_DELETED -> {
                handleDeleteInventory(message);
            }
            case STOCK_ADJUSTED -> {
                handleAddStock(message);
            }
            case STOCK_REMOVED -> {
                handleRemoveStock(message);
            }
            default -> throw new UnsupportedEventException(message.getEventType(), message.getAggregateType());
        };

        log.info("Evento inventario procesado: {}", message.getEventType());
    }

    private void processStore(StoreEventMessage message) {
        StoreEventType type = StoreEventType.valueOf(message.getEventType());

        switch (type) {
            case STORE_CREATED -> handleStoreCreate(message);
            case STORE_UPDATED -> handleStoreUpdate(message);
            case STORE_DELETED -> handleStoreDelete(message);
            case STORE_ACTIVATED -> handleStoreActivate(message);
            case STORE_DEACTIVATED -> handleStoreDeactivate(message);
        }

        log.info("Evento tienda procesado: {}", message.getEventType());
    }

    private void processProduct(ProductEventMessage message) {
        ProductEventType type = ProductEventType.valueOf(message.getEventType());

        switch (type) {
            case PRODUCT_CREATED -> handleProductCreate(message);
            case PRODUCT_UPDATED -> handleProductUpdate(message);
            case PRODUCT_DELETED -> handleProductDelete(message);
        }

        log.info("Evento producto procesado: {}", message.getEventType());
    }

    // --- Handlers de Inventario ---
    private void handleCreateInventory(InventoryEventMessage message) {
        Store store = storeRepository.findByName(message.getStore())
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada: " + message.getStore()));
        Product product = productRepository.findByCode(message.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + message.getProduct()));

        inventoryRepository.findByStoreNameAndProductCode(message.getStore(), message.getProduct())
                .ifPresent(inv -> {
                    throw new IllegalStateException("Inventario duplicado");
                });

        inventoryRepository.save(new Inventory(store, product, message.getQuantity()));
    }

    private void handleDeleteInventory(InventoryEventMessage message) {
        Inventory inventory = inventoryRepository
                .findByStoreNameAndProductCode(message.getStore(), message.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));

        inventoryRepository.delete(inventory);
    }

    private void handleAddStock(InventoryEventMessage message) {
        Inventory inventory = inventoryRepository
                .findByStoreNameAndProductCode(message.getStore(), message.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));

        if (message.getQuantity() <= 0)
            throw new IllegalArgumentException("Cantidad inválida");
        inventory.addStock(message.getQuantity());
        inventoryRepository.save(inventory);
    }

    private void handleRemoveStock(InventoryEventMessage message) {
        Inventory inventory = inventoryRepository
                .findByStoreNameAndProductCode(message.getStore(), message.getProduct())
                .orElseThrow(() -> new IllegalArgumentException("Inventario no encontrado"));

        if (message.getQuantity() <= 0)
            throw new IllegalArgumentException("Cantidad inválida");
        if (inventory.getQuantity() < message.getQuantity())
            throw new IllegalStateException("Stock insuficiente");

        inventory.removeStock(message.getQuantity());
        inventoryRepository.save(inventory);
    }

    // --- Handlers de Tienda ---
    private void handleStoreCreate(StoreEventMessage message) {
        storeRepository.findByName(message.getName())
                .ifPresent(s -> {
                    throw new IllegalStateException("Tienda duplicada");
                });
        storeRepository.save(new Store(message.getName(), message.getAddress(), true));
    }

    private void handleStoreUpdate(StoreEventMessage message) {
        Store store = storeRepository.findByName(message.getName())
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada"));
        store.setAddress(message.getAddress());
        storeRepository.save(store);
    }

    private void handleStoreDelete(StoreEventMessage message) {
        Store store = storeRepository.findByName(message.getName())
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada"));
        storeRepository.delete(store);
    }

    private void handleStoreActivate(StoreEventMessage message) {
        Store store = storeRepository.findByName(message.getName())
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada"));
        store.activate();
        storeRepository.save(store);
    }

    private void handleStoreDeactivate(StoreEventMessage message) {
        Store store = storeRepository.findByName(message.getName())
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada"));
        store.deactivate();
        storeRepository.save(store);
    }

    // --- Handlers de Producto ---
    private void handleProductCreate(ProductEventMessage message) {
        productRepository.findByCode(message.getCode())
                .ifPresent(p -> {
                    throw new IllegalStateException("Producto duplicado");
                });
        productRepository
                .save(new Product(message.getCode(), message.getName(), message.getDescription(), message.getPrice()));
    }

    private void handleProductUpdate(ProductEventMessage message) {
        Product p = productRepository.findByCode(message.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        p.setName(message.getName());
        p.setDescription(message.getDescription());
        p.setPrice(message.getPrice());
        productRepository.save(p);
    }

    private void handleProductDelete(ProductEventMessage message) {
        Product p = productRepository.findByCode(message.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        productRepository.delete(p);
    }
}