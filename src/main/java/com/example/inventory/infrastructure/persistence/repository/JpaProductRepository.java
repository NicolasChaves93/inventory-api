package com.example.inventory.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.infrastructure.persistence.entity.ProductEntity;

/**
 * Adaptador: implementa el contrato de dominio usando JPA.
 */
@Repository
public class JpaProductRepository implements ProductRepository {
	
	private final SpringDataProductRepository jpaRepository;
	
	public JpaProductRepository(SpringDataProductRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}
	
	@Override
	public Optional<Product> findByCode(String code) {
		return jpaRepository.findByCode(code)
				.map(entity -> new Product(entity.getCode(), entity.getName(), entity.getQuantity()));
	}
	
	@Override
	public void save(Product product) {
		var entity = new ProductEntity();
		entity.setCode(product.getCode());
		entity.setName(product.getName());
		entity.setQuantity(product.getQuantity());
		jpaRepository.save(entity);
	}
	

}
