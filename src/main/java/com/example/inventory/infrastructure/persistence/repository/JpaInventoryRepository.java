package com.example.inventory.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.mapper.InventoryMapper;
import com.example.inventory.domain.model.Inventory;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.infrastructure.persistence.entity.InventoryEntity;
import com.example.inventory.infrastructure.persistence.entity.ProductEntity;
import com.example.inventory.infrastructure.persistence.entity.StoreEntity;


/**
 * Adaptador: implementa el contrato de dominio usando JPA.
 */
@Repository
public class JpaInventoryRepository implements InventoryRepository {
	
	private final SpringDataInventoryRepository springDataInventoryRepository;
	private final SpringDataStoreRepository storeRepository;
	private final SpringDataProductRepository productRepository;
	
	public JpaInventoryRepository(SpringDataInventoryRepository springDataInventoryRepository,
			SpringDataStoreRepository storeRepository,
			SpringDataProductRepository productRepository) {
		this.springDataInventoryRepository = springDataInventoryRepository;
		this.storeRepository = storeRepository;
		this.productRepository = productRepository;
	}
	
	// Implementacion de metodos del repositorio
	
	@Override
	public Optional<Inventory> findByStoreNameAndProductCode(String storeName, String productCode) {
		
		return springDataInventoryRepository
				.findByStore_NameAndProduct_Code(storeName, productCode)
				.map(InventoryMapper::toDomain);
	}
	
	@Override
	public List<Inventory> findByStoreName(String storeName) {
		return springDataInventoryRepository
				.findByStore_Name(storeName)
				.stream()
				.map(InventoryMapper::toDomain)
				.toList();
	}
	
	@Override
	public List<Inventory> findByProductCode(String productCode) {
		return springDataInventoryRepository
				.findByProduct_Code(productCode)
				.stream()
				.map(InventoryMapper::toDomain)
				.toList();
	}
	
	@Override
	public void save(Inventory inventory) {
	    Optional<InventoryEntity> optionalEntity =
	            springDataInventoryRepository.findByStore_NameAndProduct_Code(
	                    inventory.getStore().getName(),
	                    inventory.getProduct().getCode()
	            );

	    InventoryEntity entity;

	    if (optionalEntity.isPresent()) {
	        entity = optionalEntity.get();
	    } else {
	        StoreEntity storeEntity = storeRepository.findByName(inventory.getStore().getName())
	                .orElseThrow(() -> new EntityNotFoundException("Tienda", inventory.getStore().getName()));

	        ProductEntity productEntity = productRepository.findByCode(inventory.getProduct().getCode())
	                .orElseThrow(() -> new EntityNotFoundException("Producto", inventory.getProduct().getCode()));

	        entity = new InventoryEntity();
	        entity.setStore(storeEntity);
	        entity.setProduct(productEntity);
	    }

	    entity.setQuantity(inventory.getQuantity());

	    springDataInventoryRepository.save(entity);
	}
	
	@Override
	public void delete(Inventory inventory) {
		springDataInventoryRepository
			.deleteByStore_NameAndProduct_Code(inventory.getStore().getName(), inventory.getProduct().getCode());
	}

}
