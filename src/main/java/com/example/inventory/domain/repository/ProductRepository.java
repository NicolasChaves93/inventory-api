package com.example.inventory.domain.repository;

import java.util.Optional;

import com.example.inventory.domain.model.Product;

/**
 * Puerto de salida: contrato de persistencia.
 * Define qué necesita el dominio para trabajar con productos.
 */
public interface ProductRepository{
	Optional<Product> findByCode(String code);
	void save(Product product);
}