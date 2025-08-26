package com.example.inventory.application.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.application.dto.InventoryItemProductResponse;
import com.example.inventory.application.dto.InventoryItemStoreResponse;
import com.example.inventory.application.dto.InventoryListProductResponse;
import com.example.inventory.application.dto.InventoryListStoreResponse;
import com.example.inventory.application.dto.InventoryResponse;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.exception.InventoryAlreadyExistsException;
import com.example.inventory.application.exception.ProductNotFoundException;
import com.example.inventory.application.exception.StoreNotFoundException;
import com.example.inventory.application.port.out.EventPublisherPort;
import com.example.inventory.config.StoreProperties;
import com.example.inventory.application.service.InventoryService;
import com.example.inventory.domain.event.InventoryEventFactory;
import com.example.inventory.domain.event.InventoryEventType;
import com.example.inventory.domain.model.Inventory;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.domain.repository.StoreRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository inventoryRepository;
	private final ProductRepository productRepository;
	private final StoreRepository storeRepository;
	private final EventPublisherPort eventPublisher;
	private final StoreProperties storeProperties;

	public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository,
			StoreRepository storeRepository, EventPublisherPort eventPublisher, StoreProperties storeProperties) {
		this.inventoryRepository = inventoryRepository;
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.eventPublisher = eventPublisher;
		this.storeProperties = storeProperties;
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryListProductResponse findByStore(String storeName) {
		// Validar que la tienda existe
		Store store = storeRepository.findByName(storeName)
				.orElseThrow(() -> new EntityNotFoundException("Tienda", storeName));

		// Consultar inventarios asociados a la tienda
		var items = inventoryRepository.findByStoreName(storeName).stream()
				.map(inv -> new InventoryItemProductResponse(inv.getProduct().getCode(), inv.getQuantity())).toList();

		return new InventoryListProductResponse(store.getName(), items);
	}

	@Override
	@Transactional(readOnly = true)
	public InventoryListStoreResponse findByProduct(String productCode) {
		// Validar existencia del producto
		var product = productRepository.findByCode(productCode)
				.orElseThrow(() -> new EntityNotFoundException("Producto", productCode));

		// Consultar inventarios
		var items = inventoryRepository.findByProductCode(productCode).stream()
				.map(inv -> new InventoryItemStoreResponse(inv.getStore().getName(), inv.getQuantity())).toList();

		return new InventoryListStoreResponse(productCode, product.getName(), items);
	}

	@Override
	public InventoryResponse addStock(String storeName, String productCode, int quantity) {

		if (quantity <= 0) {
			throw new IllegalArgumentException("La cantidad a agregar debe ser mayor que cero.");
		}
		
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + "-" + productCode));
		
		inventory.addStock(quantity);
		
        if (storeProperties.isLocal()) {
            inventoryRepository.save(inventory);
        } else if (storeProperties.isCentral()) {
            
            eventPublisher.publishToStore(storeName, InventoryEventFactory.createEvent(inventory, InventoryEventType.STOCK_ADJUSTED, storeProperties.getRole()));
        }
        
        eventPublisher.publishToStore("TIENDA_CENTRAL", InventoryEventFactory.createEvent(inventory, InventoryEventType.STOCK_ADJUSTED, storeProperties.getRole()));

		return new InventoryResponse(storeName, productCode, inventory.getQuantity());
	}

	@Override
	public InventoryResponse removeStock(String storeName, String productCode, int quantity) {

		if (quantity <= 0) {
			throw new IllegalArgumentException("La cantidad a remover debe ser mayor que cero.");
		}
		
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + "-" + productCode));

		if (inventory.getQuantity() < quantity) {
			throw new IllegalArgumentException("Stock insuficiente para remover " + quantity);
		}
			
		inventory.removeStock(quantity);
			
		if (storeProperties.isLocal()) {
			inventoryRepository.save(inventory);
	        
	     } else if (storeProperties.isCentral()) {
	            eventPublisher.publishToStore(storeName, 
	            		InventoryEventFactory.createEvent(inventory, InventoryEventType.STOCK_REMOVED, storeProperties.getRole()));
	        }
		
		eventPublisher.publishToStore("TIENDA_CENTRAL", 
        		InventoryEventFactory.createEvent(inventory, InventoryEventType.STOCK_REMOVED, storeProperties.getRole()));

		return new InventoryResponse(storeName, productCode, inventory.getQuantity());
	}

	@Override
	public int getStock(String storeName, String productCode) {
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + " - " + productCode));

		return inventory.getQuantity();
	}

	@Override
	public InventoryResponse createInventory(String storeName, String productCode, int initialStock) {

		boolean exists = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode).isPresent();
		if (exists) {
			throw new InventoryAlreadyExistsException(storeName, productCode);
		}

		if (initialStock < 0) {
			throw new IllegalArgumentException("El stock inicial no puede ser negativo.");
		}
		
		Store store = storeRepository.findByName(storeName).orElseThrow(() -> new StoreNotFoundException(storeName));
		Product product = productRepository.findByCode(productCode).orElseThrow(() -> new ProductNotFoundException(productCode));

		Inventory inventory = new Inventory(store, product, initialStock);
		
		if (storeProperties.isLocal()) {
	        inventoryRepository.save(inventory);
	        
	    } else if (storeProperties.isCentral()) {
	        eventPublisher.publishToStore(storeName, 
	        		InventoryEventFactory.createEvent(inventory, InventoryEventType.INVENTORY_CREATED, storeProperties.getRole()));
	    }
		
		eventPublisher.publishToStore("TIENDA_CENTRAL", 
        		InventoryEventFactory.createEvent(inventory, InventoryEventType.INVENTORY_CREATED, storeProperties.getRole()));

		return new InventoryResponse(storeName, productCode, initialStock);
	}

	@Override
	@Transactional
	public void deleteInventory(String storeName, String productCode) {
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + "-" + productCode));
		
		if (storeProperties.isLocal()) {
			inventoryRepository.delete(inventory);
			
		} else if (storeProperties.isCentral()) {
			eventPublisher.publishToStore(storeName, 
	        		InventoryEventFactory.createEvent(inventory, InventoryEventType.INVENTORY_DELETED, storeProperties.getRole()));
		}
		
		eventPublisher.publishToStore("TIENDA_CENTRAL", 
        		InventoryEventFactory.createEvent(inventory, InventoryEventType.INVENTORY_DELETED, storeProperties.getRole()));
	}

}
