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
	
	@NotNull(message = "La cantidad del producto no puede estar vacía")
	private Integer quantity;

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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
