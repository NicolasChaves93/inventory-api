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
import com.example.inventory.application.service.InventoryService;
import com.example.inventory.domain.event.InventoryEventFactory;
import com.example.inventory.domain.model.Inventory;
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

	public InventoryServiceImpl(InventoryRepository inventoryRepository, ProductRepository productRepository,
			StoreRepository storeRepository, EventPublisherPort eventPublisher) {
		this.inventoryRepository = inventoryRepository;
		this.productRepository = productRepository;
		this.storeRepository = storeRepository;
		this.eventPublisher = eventPublisher;
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
	public InventoryResponse addStock(String storeName, String productCode, int amount) {
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + "-" + productCode));

		if (amount <= 0) {
			throw new IllegalArgumentException("La cantidad a agregar debe ser mayor que cero.");
		}

		var event = InventoryEventFactory.stockAdjusted(storeName, productCode, amount);
		eventPublisher.publish(event);

		return new InventoryResponse(storeName, productCode, inventory.getQuantity() + amount);
	}

	@Override
	public InventoryResponse removeStock(String storeName, String productCode, int amount) {
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + "-" + productCode));

		if (amount <= 0) {
			throw new IllegalArgumentException("La cantidad a remover debe ser mayor que cero.");
		}

		if (inventory.getQuantity() < amount) {
			throw new IllegalArgumentException("Stock insuficiente para remover " + amount);
		}

		var event = InventoryEventFactory.stockRemoved(storeName, productCode, amount);
		eventPublisher.publish(event);

		return new InventoryResponse(storeName, productCode, inventory.getQuantity() - amount);
	}

	@Override
	public int getStock(String storeName, String productCode) {
		Inventory inventory = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + " - " + productCode));

		return inventory.getQuantity();
	}

	@Override
	public InventoryResponse createInventory(String storeName, String productCode, int initialStock) {
		storeRepository.findByName(storeName).orElseThrow(() -> new StoreNotFoundException(storeName));

		productRepository.findByCode(productCode).orElseThrow(() -> new ProductNotFoundException(productCode));

		boolean exists = inventoryRepository.findByStoreNameAndProductCode(storeName, productCode).isPresent();
		if (exists) {
			throw new InventoryAlreadyExistsException(storeName, productCode);
		}

		if (initialStock < 0) {
			throw new IllegalArgumentException("El stock inicial no puede ser negativo.");
		}

		var event = InventoryEventFactory.inventoryCreated(storeName, productCode, initialStock);
		eventPublisher.publish(event);

		return new InventoryResponse(storeName, productCode, initialStock);
	}

	@Override
	@Transactional
	public void deleteInventory(String storeName, String productCode) {
		inventoryRepository.findByStoreNameAndProductCode(storeName, productCode)
				.orElseThrow(() -> new EntityNotFoundException("Inventario", storeName + "-" + productCode));

		var event = InventoryEventFactory.inventoryDeleted(storeName, productCode);
		eventPublisher.publish(event);
	}

}
