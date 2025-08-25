package com.example.inventory.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.inventory.domain.model.Inventory;

public interface InventoryRepository {
	
	Optional<Inventory> findByStoreNameAndProductCode(String storeName, String productCode);
	
	List<Inventory> findByStoreName(String storeName);
	
	List<Inventory> findByProductCode(String productCode);
	
	void save(Inventory inventory);
	
	void delete(Inventory inventory);

}
