package com.example.inventory.application.dto;

public class StoreResponse {
	private String name;
	private String address;
	private boolean status;
	
	public StoreResponse(String name, String address, boolean status) {
		this.name = name;
		this.address = address;
		this.status = status;
	}

	// Getters
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public boolean isStatus() {
		return status;
	}
		
}
