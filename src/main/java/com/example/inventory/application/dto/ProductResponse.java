package com.example.inventory.application.dto;

/**
 * DTO de salida (response) hacia la API REST.
 */
public class ProductResponse {
	private String code;
	private String name;
	private Integer quantity;
	
	public ProductResponse(String code, String name, Integer quantity) {
		this.code = code;
		this.name = name;
		this.quantity = quantity;
	}
	
	// Getters
	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Integer getQuantity() {
		return quantity;
	}
}
