package com.example.inventory.application.service.impl;

import org.springframework.stereotype.Service;

import com.example.inventory.application.dto.ProductRequest;
import com.example.inventory.application.dto.ProductResponse;
import com.example.inventory.application.service.ProductService;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.repository.ProductRepository;
/**
 * Caso de uso: creación y consulta de productos.
 */
@Service
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository productRepository;
	
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Override
	public ProductResponse findByCode(String code) {
		return productRepository.findByCode(code)
				.map(product -> new ProductResponse(product.getCode(), product.getName(), product.getDescription(), product.getPrice()))
				.orElseThrow(() -> new RuntimeException("Producto no encontrado " + code));
	}
	
	@Override
	public void createProduct(ProductRequest request) {
		Product p = new Product(request.getCode(), request.getName(), request.getDescription(), request.getPrice());
		productRepository.save(p);
		
	}

}
