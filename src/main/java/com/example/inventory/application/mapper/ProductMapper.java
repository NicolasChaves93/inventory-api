package com.example.inventory.application.mapper;

import com.example.inventory.domain.model.Product;
import com.example.inventory.infrastructure.persistence.entity.ProductEntity;

/**
 * Convierte entre entidad de persistencia y modelo de dominio.
 */
public class ProductMapper {
	
	public static Product toDomain(ProductEntity entity) {
		return new Product(entity.getCode(), entity.getName(), entity.getDescription(), entity.getPrice());
	}

	public static ProductEntity toEntity(Product product) {
		ProductEntity entity = new ProductEntity();
		entity.setCode(product.getCode());
		entity.setName(product.getName());
		entity.setDescription(product.getDescription());
		entity.setPrice(product.getPrice());
		return entity;
	}

}
