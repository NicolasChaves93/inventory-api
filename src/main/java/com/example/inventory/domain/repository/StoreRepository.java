	package com.example.inventory.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.inventory.domain.model.Store;

/**
 * Puerto de salida: contrato de persistencia.
 * Define qué necesita el dominio para trabajar con tiendas.
 */
public interface StoreRepository {
	
	void save(Store store);
	
	Optional<Store> findByName(String name);
	
	List<Store> findAll();
	
	void delete(Store store);

}
