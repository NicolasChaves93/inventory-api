package com.example.inventory.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.inventory.application.mapper.StoreMapper;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.StoreRepository;
import com.example.inventory.infrastructure.persistence.entity.StoreEntity;

import jakarta.transaction.Transactional;

/**
 * Adaptador: implementa el contrato de dominio usando JPA.
 */

@Repository
public class JpaStoreRepository implements StoreRepository {
	
	private final SpringDataStoreRepository jpaRepository;
	
	public JpaStoreRepository(SpringDataStoreRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}
	
	// métodos del repositorio de la tienda
	
	@Override
	public Optional<Store> findByName(String name) {
		return jpaRepository.findByName(name)
				.map(StoreMapper::toDomain);
	}
	
	@Override
	public void save(Store store) {
	    StoreEntity entity = jpaRepository.findByName(store.getName())
	        .orElse(new StoreEntity());

	    entity.setName(store.getName());
	    entity.setAddress(store.getAddress());
	    entity.setStatus(store.isStatus());

	    jpaRepository.save(entity);
	}
	
	@Override
	@Transactional
	public void delete(Store store) {
		jpaRepository.deleteByName(store.getName());
	}
	
	@Override
	public List<Store> findAll() {
		return jpaRepository.findAll().stream()
				.map(StoreMapper::toDomain)
				.toList();
	}

}
