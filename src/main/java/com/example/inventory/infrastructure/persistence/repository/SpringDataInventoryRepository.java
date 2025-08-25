package com.example.inventory.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.infrastructure.persistence.entity.InventoryEntity;

/**
 * Spring Data JPA repository for Inventory entities.
 * This interface will extend JpaRepository to provide CRUD operations and custom queries.
 */
public interface SpringDataInventoryRepository extends JpaRepository<InventoryEntity, Long> {
	
	Optional<InventoryEntity> findByStore_NameAndProduct_Code(String storeName, String productCode);
	
	List<InventoryEntity> findByStore_Name(String storeName);
	
	List<InventoryEntity> findByProduct_Code(String productCode);
	
	void deleteByStore_NameAndProduct_Code(String storeName, String productCode);

}
