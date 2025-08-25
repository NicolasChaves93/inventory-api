package com.example.inventory.domain.model;

import com.example.inventory.application.exception.InventoryBusinessException;

/**
 * Inventory domain model class.
 * Represents the inventory of products in the system.
 */

public class Inventory {
	
	private final Store store;
    private final Product product;
    private int quantity;
    
    public Inventory(Store store, Product product, int initialQuantity) {
        if (store == null) throw new InventoryBusinessException("La tienda no puede ser nula");
        if (product == null) throw new InventoryBusinessException("El producto no puede ser nulo");
        if (initialQuantity < 0) throw new InventoryBusinessException("La cantidad inicial no puede ser negativa");

        this.store = store;
        this.product = product;
        this.quantity = initialQuantity;
    }
    
    // Getters
    public Store getStore() { return store; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    // Modificación de inventario con reglas de negocio
    public void addStock(int amount) {
        if (amount <= 0) throw new InventoryBusinessException("La cantidad a agregar debe ser mayor a cero");
        this.quantity += amount;
    }

    public void removeStock(int amount) {
        if (amount <= 0) throw new InventoryBusinessException("La cantidad a remover debe ser mayor a cero");
        if (amount > this.quantity) throw new InventoryBusinessException("No se puede remover más de la cantidad disponible");
        this.quantity -= amount;
    }
}
