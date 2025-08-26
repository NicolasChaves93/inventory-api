package com.example.inventory.infrastructure.messaging.dto;

import com.example.inventory.domain.event.EventCategory;

public class InventoryEventMessage extends DomainEventMessage {

    private String store;
    private String product;
    private int quantity;

    public InventoryEventMessage() {
    }

    // Getters y setters
    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
