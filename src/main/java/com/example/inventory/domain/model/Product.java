package com.example.inventory.domain.model;

/**
 * Clase de entidad producto.
 */
public class Product {
	private final String code;
	private String name;
	private String description;
	private float price;
	
	public Product(String code, String name, String description, float price) {
		if ( code == null || code.isBlank()) {
			throw new IllegalArgumentException("El código es obligatorio");
		}
		
		this.code = code;
		this.name = name;
		this.description = description;
		this.price = price;
	}
	
	// Reglas de negocio
	public void updatePrice(float newPrice) {
		if (newPrice < 0) {
			throw new IllegalArgumentException("El precio no puede ser negativo");
		}
		this.price = newPrice;
	}
	
	// Getters
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public float getPrice() {
		return price;
	}
}
