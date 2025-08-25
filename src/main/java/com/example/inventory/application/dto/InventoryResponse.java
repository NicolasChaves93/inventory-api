package com.example.inventory.application.dto;

/**
 * DTO de salida (response) hacia la API REST.
 */
public class InventoryResponse {
	private String storeName;
	private String productCode;
	private int quantity;
	
	public InventoryResponse(String storeName, String productCode, int quantity) {
		this.storeName = storeName;
		this.productCode = productCode;
		this.quantity = quantity;
	}
	
	// Getters
	public String getStoreName() {
		return storeName;
	}

	public String getProductCode() {
		return productCode;
	}

	public int getQuantity() {
		return quantity;
	}

}
