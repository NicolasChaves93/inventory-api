package com.example.inventory.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada (request) desde la API REST.
 */
public class ProductRequest {
	@NotBlank(message = "El codigo del producto no puede estar vacío")
	private String code;
	
	@NotBlank(message = "El nombre del producto no puede estar vacío")
	private String name;
	
	private String description;
	
	@NotNull(message = "El precio del producto es obligatorio")
	private float price;

	// Getters and Setters
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
}
