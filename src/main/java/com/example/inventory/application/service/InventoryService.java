package com.example.inventory.application.service;

import com.example.inventory.application.dto.InventoryListProductResponse;
import com.example.inventory.application.dto.InventoryListStoreResponse;
import com.example.inventory.application.dto.InventoryResponse;

/**
 * Caso de uso para la gestión de inventario. Orquesta puertos y lógica de negocio.
 */
public interface InventoryService {
	
	InventoryListProductResponse findByStore(String storeName);
	
	InventoryListStoreResponse findByProduct(String productCode);
	
	InventoryResponse addStock(String storeName, String productCode, int amount);
	
	InventoryResponse removeStock(String storeName, String productCode, int amount);
	
	int getStock(String storeName, String productCode);
	
	InventoryResponse createInventory(String storeName, String productCode, int initialStock);
	
	void deleteInventory(String storeName, String productCode);

}
