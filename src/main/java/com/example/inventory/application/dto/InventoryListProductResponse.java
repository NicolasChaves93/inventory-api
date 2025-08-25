package com.example.inventory.application.dto;

import java.util.List;

public class InventoryListProductResponse {
	
	private String storeName;
    private List<InventoryItemProductResponse> items;
    
    public InventoryListProductResponse(String storeName, List<InventoryItemProductResponse> items) {
    	this.storeName = storeName;
		this.items = items;
    }
    
    public String getStoreName() { return storeName; }
    public List<InventoryItemProductResponse> getItems() { return items; }

}
