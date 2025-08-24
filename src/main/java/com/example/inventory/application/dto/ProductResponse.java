package com.example.inventory.application.dto;

/**
 * DTO de salida (response) hacia la API REST.
 */
public class ProductResponse {
	private String code;
	private String name;
	private String description;
	private float price;
	
	public ProductResponse(String code, String name, String description, float price) {
		this.code = code;
		this.name = name;
		this.description = description;
		this.price = price;
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
