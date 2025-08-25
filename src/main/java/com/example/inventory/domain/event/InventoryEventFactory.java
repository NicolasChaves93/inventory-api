package com.example.inventory.domain.event;

public class InventoryEventFactory {

    private InventoryEventFactory() {}

    public static DomainEvent inventoryCreated(String store, String product, int qty) {
        return DomainEventFactory.create(
                InventoryEventType.INVENTORY_CREATED.name(),
                "Inventory",
                store + "-" + product,
                """
                {"store":"%s","product":"%s","quantity":%d,"event":"%s"}
                """.formatted(store, product, qty, InventoryEventType.INVENTORY_CREATED.name())
        );
    }

    public static DomainEvent inventoryDeleted(String store, String product) {
        return DomainEventFactory.create(
                InventoryEventType.INVENTORY_DELETED.name(),
                "Inventory",
                store + "-" + product,
                """
                {"store":"%s","product":"%s","event":"%s"}
                """.formatted(store, product, InventoryEventType.INVENTORY_DELETED.name())
        );
    }

    public static DomainEvent stockAdjusted(String store, String product, int qty) {
        return DomainEventFactory.create(
                InventoryEventType.STOCK_ADJUSTED.name(),
                "Inventory",
                store + "-" + product,
                """
                {"store":"%s","product":"%s","quantity":%d,"event":"%s"}
                """.formatted(store, product, qty, InventoryEventType.STOCK_ADJUSTED.name())
        );
    }
    
    public static DomainEvent stockRemoved(String store, String product, int qty) {
        return DomainEventFactory.create(
                InventoryEventType.STOCK_REMOVED.name(),
                "Inventory",
                store + "-" + product,
                """
                {"store":"%s","product":"%s","quantity":%d,"event":"%s"}
                """.formatted(store, product, qty, InventoryEventType.STOCK_REMOVED.name())
        );
    }
}