package com.example.inventory.application.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.inventory.application.dto.ProductRequest;
import com.example.inventory.application.dto.ProductResponse;
import com.example.inventory.application.exception.DuplicateCodeException;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.service.ProductService;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.repository.ProductRepository;
import com.example.inventory.config.StoreProperties;
import com.example.inventory.application.port.out.EventPublisherPort;
import com.example.inventory.domain.event.ProductEventFactory;
import com.example.inventory.domain.event.ProductEventType;
/**
 * Caso de uso: creación y consulta de productos.
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final String ENTITY_NAME = "Producto";
	
	private final ProductRepository productRepository;
	private final StoreProperties storeProperties;
	private final EventPublisherPort eventPublisher;

	public ProductServiceImpl(ProductRepository productRepository, StoreProperties storeProperties, EventPublisherPort eventPublisher) {
		this.productRepository = productRepository;
		this.storeProperties = storeProperties;
		this.eventPublisher = eventPublisher;
	}
	
	private void validateDuplicateCode(String code) {
	    productRepository.findByCode(code)
	        .ifPresent(p -> {
	            throw new DuplicateCodeException(code);
	        });
	}
	
	@Override
	public ProductResponse findByCode(String code) {
		return productRepository.findByCode(code)
				.map(product -> new ProductResponse(product.getCode(), product.getName(), product.getDescription(), product.getPrice()))
				.orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, code));
	}
	
	
	@Override
	public void createProduct(ProductRequest request) {
		validateDuplicateCode(request.getCode());
		
		Product p = new Product(request.getCode(), request.getName(), request.getDescription(), request.getPrice());
		if (storeProperties.isLocal()) {
			productRepository.save(p);
		}

		// Publicar a todas las tiendas siempre
        eventPublisher.publishToAll(
            ProductEventFactory.createEvent(p, ProductEventType.PRODUCT_CREATED, storeProperties.getRole()));
	}
	
	@Override
	public void updateProduct(String code, ProductRequest request) {
		Product existingProduct = productRepository.findByCode(code)
				.orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, code));
		
		existingProduct.setName(request.getName());
		existingProduct.setDescription(request.getDescription());
		existingProduct.setPrice(request.getPrice());
		
		if (storeProperties.isLocal()) {
			productRepository.save(existingProduct);
		}

		eventPublisher.publishToAll(
	            ProductEventFactory.createEvent(existingProduct, ProductEventType.PRODUCT_UPDATED, storeProperties.getRole()));
	}
	
	@Override
	public void deleteProduct(String code) {
		Product existingProduct = productRepository.findByCode(code)
				.orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, code));
		
		if (storeProperties.isLocal()) {
			productRepository.delete(existingProduct);
		}
		
		eventPublisher.publishToAll(
	            ProductEventFactory.createEvent(existingProduct, ProductEventType.PRODUCT_DELETED, storeProperties.getRole()));
	}
	
	@Override
	public List<ProductResponse> findAll() {
		return productRepository.findAll().stream()
				.map(product -> new ProductResponse(product.getCode(), product.getName(), product.getDescription(), product.getPrice()))
				.toList();
	}

}
