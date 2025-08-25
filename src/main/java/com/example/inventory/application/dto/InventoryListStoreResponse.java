package com.example.inventory.application.dto;

import java.util.List;

/**
 * DTO para la respuesta de inventario de una tienda.
 */
public class InventoryListStoreResponse {
	
	private String productCode;
	private String productName;
	private List<InventoryItemStoreResponse> stores;
	
	public InventoryListStoreResponse(String productCode, String productName, List<InventoryItemStoreResponse> stores) {
		this.productCode = productCode;
		this.productName = productName;
		this.stores = stores;
	}
	
	public String getProductCode() { return productCode; }
	public String getProductName() { return productName; }
	public List<InventoryItemStoreResponse> getStores() { return stores; }

}
