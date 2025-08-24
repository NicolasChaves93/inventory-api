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
				.orElseThrow(() -> new EntityNotFoundException("Producto", code));
	}
	
	@Override
	public void createProduct(ProductRequest request) {
		if (productRepository.findByCode(request.getCode()).isPresent()) {
			throw new DuplicateCodeException(request.getCode());
		}
		Product p = new Product(request.getCode(), request.getName(), request.getDescription(), request.getPrice());
		productRepository.save(p);
	}
	
	@Override
	public void updateProduct(String code, ProductRequest request) {
		Product existingProduct = productRepository.findByCode(code)
				.orElseThrow(() -> new EntityNotFoundException("Producto", code));
		
		existingProduct.setName(request.getName());
		existingProduct.setDescription(request.getDescription());
		existingProduct.setPrice(request.getPrice());
		
		productRepository.save(existingProduct);
	}
	
	@Override
	public void deleteProduct(String code) {
		Product existingProduct = productRepository.findByCode(code)
				.orElseThrow(() -> new EntityNotFoundException("Producto", code));
		productRepository.delete(existingProduct);
	}
	
	@Override
	public List<ProductResponse> findAll() {
		return productRepository.findAll().stream()
				.map(product -> new ProductResponse(product.getCode(), product.getName(), product.getDescription(), product.getPrice()))
				.toList();
	}

}
