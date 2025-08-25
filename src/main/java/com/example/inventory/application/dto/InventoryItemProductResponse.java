package com.example.inventory.application.dto;

/**
 * DTO para la respuesta de inventario.
 */
public class InventoryItemProductResponse {
	
	private String productCode;
    private int quantity;
    
    public InventoryItemProductResponse(String productCode, int quantity) {
		this.productCode = productCode;
		this.quantity = quantity;
	}
	
	public String getProductCode() { return productCode; }
	public int getQuantity() { return quantity; }

}
