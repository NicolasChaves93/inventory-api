package com.example.inventory.application.exception;

/**
 * Excepción lanzada cuando no se encuentra un producto.
 */
public class ProductNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String productCode) {
		super("El producto con código '" + productCode + "' no existe.");
	}

}
