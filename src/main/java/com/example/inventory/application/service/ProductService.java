package com.example.inventory.application.service;

import com.example.inventory.application.dto.ProductRequest;
import com.example.inventory.application.dto.ProductResponse;

/**
 * Caso de uso para la gestión de productos. Orquesta puertos y lógica de negocio.
 */
public interface ProductService {
	ProductResponse findByCode(String code);
	void createProduct(ProductRequest request);
	

}
