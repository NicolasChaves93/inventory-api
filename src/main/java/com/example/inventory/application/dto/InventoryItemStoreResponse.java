package com.example.inventory.application.dto;

/**
 * DTO para la respuesta de inventario en una tienda específica.
 */
public class InventoryItemStoreResponse {
	
	private String storeName;
	private int quantity;
	
	public InventoryItemStoreResponse(String storeName, int quantity) {
		this.storeName = storeName;
		this.quantity = quantity;
	}
	
	public String getStoreName() { return storeName; }
	public int getQuantity() { return quantity; }

}
