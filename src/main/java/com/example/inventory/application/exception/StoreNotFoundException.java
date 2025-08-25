package com.example.inventory.application.exception;

/**
 * Excepción lanzada cuando no se encuentra una tienda.
 */
public class StoreNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public StoreNotFoundException(String storeName) {
		super("La tienda '" + storeName + "' no existe.");
	}

}
