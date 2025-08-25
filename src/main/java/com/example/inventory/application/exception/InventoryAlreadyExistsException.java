package com.example.inventory.application.exception;


/**
 * Excepción lanzada cuando se intenta crear un inventario que ya existe.
 */
public class InventoryAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InventoryAlreadyExistsException(String storeName, String productCode) {
		super("El inventario para la tienda '" + storeName + "' y el producto '" + productCode + "' ya existe.");
	}

}
