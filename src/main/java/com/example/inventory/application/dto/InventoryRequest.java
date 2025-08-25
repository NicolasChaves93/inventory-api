package com.example.inventory.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada (request) desde la API REST para operaciones de inventario.
 */
public class InventoryRequest {
	
	@NotBlank(message = "El nombre de la tienda no puede estar vacío")
	private String storeName;
	
	@NotBlank(message = "El código del producto no puede estar vacío")
	private String productCode;
	
	@NotNull(message = "La cantidad es obligatoria")
	@Min(value = 0, message = "La cantidad no puede ser menor que cero")
	private int quantity;

	// Getters and Setters
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
