package com.example.inventory.domain.model;

/**
 * Entidad de dominio Store.
 */

public class Store {
	
	private String name;
	private String address;
	private boolean status;
	
	public Store(String name, String address, boolean status) {
		if ( name == null || name.isBlank()) {
			throw new IllegalArgumentException("El nombre es obligatorio");
		}
		
		this.name = name;
		this.address = address;
		this.status = status;
	}
	
	// Reglas de negocio
	public void deactivate() {
		this.status = false;
	}
	
	public void activate() {
		this.status = true;
	}
	
	// Getters and setters
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public boolean isStatus() {
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
