package com.example.inventory.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.infrastructure.persistence.entity.ProductEntity;

/**
 * Repositorio de productos utilizando Spring Data JPA.
 * Define consultas adicionales necesarias para persistencia.
 */
public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long>{
	
	Optional<ProductEntity> findByCode(String code);
	
	boolean existsByCode(String code);
	
	void deleteByCode(String code);

}
