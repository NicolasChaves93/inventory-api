package com.example.inventory.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.infrastructure.persistence.entity.ProductEntity;

/**
 * Repositorio de productos utilizando Spring Data JPA.
 */
public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long>{
	Optional<ProductEntity> findByCode(String code);

}
