package com.example.inventory.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.inventory.application.mapper.ProductMapper;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.infrastructure.persistence.entity.ProductEntity;

import jakarta.transaction.Transactional;

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
				.map(ProductMapper::toDomain);
	}
	
	@Override
	public void save(Product product) {
		ProductEntity entity = jpaRepository.findByCode(product.getCode())
	        .orElse(new ProductEntity());
		
		entity.setCode(product.getCode());
		entity.setName(product.getName());
		entity.setDescription(product.getDescription());
		entity.setPrice(product.getPrice());
		jpaRepository.save(entity);
	}
	
	
	@Override
	@Transactional
	public void delete(Product product) {
		jpaRepository.deleteByCode(product.getCode());
	}
	
	@Override
	public List<Product> findAll() {
		return jpaRepository.findAll().stream()
				.map(ProductMapper::toDomain)
				.toList();
	}

}
