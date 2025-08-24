package com.example.inventory.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory.infrastructure.persistence.entity.StoreEntity;

/**
 * Repositorio de tiendas utilizando Spring Data JPA.
 * Define consultas adicionales necesarias para persistencia.
 */
public interface SpringDataStoreRepository extends JpaRepository<StoreEntity, Long>{
	
	Optional<StoreEntity> findByName(String name);
	
	boolean existsByName(String name);
	
	void deleteByName(String name);

}
