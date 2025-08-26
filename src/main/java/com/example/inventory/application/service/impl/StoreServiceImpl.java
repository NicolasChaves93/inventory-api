package com.example.inventory.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.inventory.application.dto.StoreRequest;
import com.example.inventory.application.dto.StoreResponse;
import com.example.inventory.application.exception.DuplicateNameException;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.service.StoreService;
import com.example.inventory.config.StoreProperties;
import com.example.inventory.application.exception.StoreNameRequiredException;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.StoreRepository;
import com.example.inventory.application.port.out.EventPublisherPort;
import com.example.inventory.domain.event.StoreEventFactory;
import com.example.inventory.domain.event.StoreEventType;

import jakarta.transaction.Transactional;

/**
 * Caso de uso: gestión de tiendas.
 */

@Service
public class StoreServiceImpl implements StoreService {
	
	private final StoreRepository storeRepository;
	private final StoreProperties storeProperties;
	private final EventPublisherPort eventPublisher;

	public StoreServiceImpl(StoreRepository storeRepository, StoreProperties storeProperties, EventPublisherPort eventPublisher) {
		this.storeRepository = storeRepository;
		this.storeProperties = storeProperties;
		this.eventPublisher = eventPublisher;
	}
	
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
        
        if (storeProperties.isLocal()) {
            storeRepository.save(store);
        }

        // Publicar evento hacia central
        eventPublisher.publishToStore("TIENDA_CENTRAL",
                StoreEventFactory.createEvent(store, StoreEventType.STORE_CREATED, storeProperties.getRole()));
        
        eventPublisher.publishToStore(request.getName(),
				StoreEventFactory.createEvent(store, StoreEventType.STORE_CREATED, storeProperties.getRole()));
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
        Store existingStore = storeRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada: " + name));

        String newName = request.getName();

        // Validaciones
        if (storeProperties.isCentral() && (newName == null || newName.isBlank())) {
            throw new StoreNameRequiredException();
        }
        if (newName != null && !newName.equals(name) &&
            storeRepository.findByName(newName).isPresent()) {
            throw new DuplicateNameException(newName);
        }

        // Actualizar dirección
        existingStore.setAddress(request.getAddress());

        // Actualizar nombre si aplica
        if (newName != null && !newName.isBlank()) {
            existingStore.setName(newName);
        }

        // Solo las tiendas locales hacen persistencia directamente
        if (storeProperties.isLocal()) {
            storeRepository.save(existingStore);
        }

        // Publicar evento hacia central
        // Todas las operaciones de Store van hacia Central
        eventPublisher.publishToStore(
        		"TIENDA_CENTRAL",
                StoreEventFactory.createEvent(existingStore, StoreEventType.STORE_UPDATED, storeProperties.getRole()));
        
        eventPublisher.publishToStore(request.getName(),
				StoreEventFactory.createEvent(existingStore, StoreEventType.STORE_UPDATED, storeProperties.getRole()));
    }
	
	@Override
	@Transactional
	public void deactivateStore(String name) {
		Store existingStore = storeRepository.findByName(name)
			.orElseThrow(() -> new EntityNotFoundException("Tienda", name));

		if (!existingStore.isStatus()) {
			return;
		}
		
		existingStore.deactivate();
		if (storeProperties.isLocal()) {
			storeRepository.save(existingStore);
		}
		
		// Notificar a central
        eventPublisher.publishToStore("TIENDA_CENTRAL",
            StoreEventFactory.createEvent(existingStore, StoreEventType.STORE_DEACTIVATED, storeProperties.getRole()));
        
        eventPublisher.publishToStore(name,
				StoreEventFactory.createEvent(existingStore, StoreEventType.STORE_DEACTIVATED, storeProperties.getRole()));
	}

	
	@Override
	@Transactional
	public void activateStore(String name) {
		Store existingStore = storeRepository.findByName(name)
			.orElseThrow(() -> new EntityNotFoundException("Tienda", name));

		if (existingStore.isStatus()) {
			return; 
		}
		
		existingStore.activate();
		if (storeProperties.isLocal()) {
			storeRepository.save(existingStore);
		}
		
		// Notificar a central
        eventPublisher.publishToStore("TIENDA_CENTRAL",
            StoreEventFactory.createEvent(existingStore, StoreEventType.STORE_ACTIVATED, storeProperties.getRole()));
        
        eventPublisher.publishToStore(name,
				StoreEventFactory.createEvent(existingStore, StoreEventType.STORE_ACTIVATED, storeProperties.getRole()));
	}

	@Override
	public void deleteStore(String name) {
		Store store = storeRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tienda no encontrada: " + name));
		
		if (storeProperties.isLocal()) {
            storeRepository.delete(store);
        }
		
		// Eliminar notifica a Central
        eventPublisher.publishToStore("TIENDA_CENTRAL",
                StoreEventFactory.createEvent(store, StoreEventType.STORE_DELETED, storeProperties.getRole()));
        
        eventPublisher.publishToStore(name,
				StoreEventFactory.createEvent(store, StoreEventType.STORE_DELETED, storeProperties.getRole()));
	}
}
