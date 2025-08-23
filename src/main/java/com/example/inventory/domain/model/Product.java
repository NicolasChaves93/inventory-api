package com.example.inventory.domain.model;

/**
 * Clase de entidad producto.
 */
public class Product {
	private final String code;
	private String name;
	private int quantity;
	
	public Product(String code, String name, int quantity) {
		if ( code == null || code.isBlank()) {
			throw new IllegalArgumentException("El código es obligatorio");
		}
		this.code = code;
		this.name = name;
		this.quantity = quantity;
	}
	
	// Reglas de negocio
	public void increaseStock(int amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Cantidad invalida");
		}
		this.quantity += amount;
	}

	
	// Getters
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public int getQuantity() {
		return quantity;
	}
	
}
