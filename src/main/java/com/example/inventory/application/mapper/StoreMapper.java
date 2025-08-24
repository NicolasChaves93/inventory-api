package com.example.inventory.application.mapper;

import com.example.inventory.domain.model.Store;
import com.example.inventory.infrastructure.persistence.entity.StoreEntity;

/**
 * Convierte entre entidad de persistencia y modelo de dominio.
 */
public class StoreMapper {
	
	public static Store toDomain(StoreEntity entity) {
        return new Store(entity.getName(), entity.getAddress(), entity.isStatus());
    }

    public static StoreEntity toEntity(Store store) {
        StoreEntity entity = new StoreEntity();
        entity.setName(store.getName());
        entity.setAddress(store.getAddress());
        entity.setStatus(store.isStatus());
        return entity;
    }

}
