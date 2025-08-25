package com.example.inventory.application.mapper;

import com.example.inventory.domain.model.Inventory;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.Store;
import com.example.inventory.infrastructure.persistence.entity.InventoryEntity;
import com.example.inventory.infrastructure.persistence.entity.ProductEntity;
import com.example.inventory.infrastructure.persistence.entity.StoreEntity;

/**
 * Convierte entre entidad de persistencia y modelo de dominio.
 */
public class InventoryMapper {
	
	// Convierte de entidad de persistencia a modelo de dominio
    public static Inventory toDomain(InventoryEntity entity) {
        // Crear objetos de dominio para store y product usando sus constructores completos
        Store store = new Store(
                entity.getStore().getName(),
                entity.getStore().getAddress(),
                entity.getStore().isStatus()
        );

        Product product = new Product(
                entity.getProduct().getCode(),
                entity.getProduct().getName(),
                entity.getProduct().getDescription(),
                entity.getProduct().getPrice()
        );

        return new Inventory(store, product, entity.getQuantity());
    }

    // Convierte de modelo de dominio a entidad de persistencia
    public static InventoryEntity toEntity(Inventory inventory) {
        InventoryEntity entity = new InventoryEntity();

        // Convertir objetos de dominio a entidades JPA
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(inventory.getStore().getName());
        storeEntity.setAddress(inventory.getStore().getAddress());
        storeEntity.setStatus(inventory.getStore().isStatus());

        ProductEntity productEntity = new ProductEntity();
        productEntity.setCode(inventory.getProduct().getCode());
        productEntity.setName(inventory.getProduct().getName());
        productEntity.setDescription(inventory.getProduct().getDescription());
        productEntity.setPrice(inventory.getProduct().getPrice());

        entity.setStore(storeEntity);
        entity.setProduct(productEntity);
        entity.setQuantity(inventory.getQuantity());

        return entity;
    }
	
    // Actualiza una entidad existente con datos del modelo de dominio
    public static void updateEntity(InventoryEntity entity, Inventory inventory) {
        entity.setQuantity(inventory.getQuantity());
    }

}
