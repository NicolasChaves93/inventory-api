package com.example.inventory.application.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada (request) desde la API REST.
 */
public class StoreRequest {
	
	@NotBlank(message = "El nombre de la tienda no puede estar vacío")
	private String name;
	
	private String address;
	
	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
