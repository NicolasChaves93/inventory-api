package com.example.inventory.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.inventory.application.dto.StoreRequest;
import com.example.inventory.application.dto.StoreResponse;
import com.example.inventory.application.exception.DuplicateNameException;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.service.StoreService;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.StoreRepository;

import jakarta.transaction.Transactional;

/**
 * Caso de uso: gestión de tiendas.
 */

@Service
public class StoreServiceImpl implements StoreService {
	
	private final StoreRepository storeRepository;
	
	public StoreServiceImpl(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}
	
	// Implementación de los métodos del servicio de tienda
	
	@Override
	public List<StoreResponse> findAll() {
		// Implementación del método para obtener todas las tiendas
		return storeRepository.findAll().stream()
				.map(store -> new StoreResponse(store.getName(), store.getAddress(), store.isStatus()))
				.toList();
	}
	
	@Override
	public void createStore(StoreRequest request) {
		
		if (storeRepository.findByName(request.getName()).isPresent()) {
			throw new DuplicateNameException(request.getName());
		}
		Store store = new Store(request.getName(), request.getAddress(), true);
		storeRepository.save(store);
	}
	
	@Override
	public StoreResponse findByName(String name) {
		// Implementación del método para encontrar una tienda por su nombre
		return storeRepository.findByName(name)
				.map(store -> new StoreResponse(store.getName(), store.getAddress(), store.isStatus()))
				.orElseThrow(() -> new EntityNotFoundException("Tienda", name));
	}
	
	@Override
	@Transactional
	public void updateStore(String name, StoreRequest request) {
		// Buscar tienda existente
        Store existingStore = storeRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("Tienda", name));
        
        // Validar unicidad del nuevo nombre
        if (!request.getName().equals(name) &&
            storeRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateNameException(request.getName());
        }
		
        // Actualizar solo los campos permitidos
        existingStore.setAddress(request.getAddress());
        if (request.getName() != null) {
            existingStore = new Store(request.getName(), request.getAddress(), existingStore.isStatus());
            existingStore.setStatus(existingStore.isStatus());
        }
		
		storeRepository.save(existingStore);
	}
	
	@Override
	@Transactional
	public void deactivateStore(String name) {
	    // Buscar tienda por nombre
	    Store existingStore = storeRepository.findByName(name)
	        .orElseThrow(() -> new EntityNotFoundException("Tienda", name));

	    // Validar si ya está desactivada
	    if (!existingStore.isStatus()) {
	        return;
	    }

	    // Desactivar tienda
	    existingStore.deactivate();
	    storeRepository.save(existingStore);
	}

	
	@Override
	@Transactional
	public void activateStore(String name) {
	    // Buscar tienda por nombre
	    Store existingStore = storeRepository.findByName(name)
	        .orElseThrow(() -> new EntityNotFoundException("Tienda", name));

	    // Validar si ya está activa
	    if (existingStore.isStatus()) {
	        return; 
	    }

	    // Activar tienda
	    existingStore.activate();
	    storeRepository.save(existingStore);
	}

	
	@Override
	public void deleteStore(String name) {
		// Implementación del método para eliminar una tienda
		Store existingStore = storeRepository.findByName(name)
				.orElseThrow(() -> new EntityNotFoundException("Tienda", name));
		
		storeRepository.delete(existingStore);
	}
}
