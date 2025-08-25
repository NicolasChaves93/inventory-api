package com.example.inventory.application.exception;

/**
 * Excepción personalizada para errores de negocio en la gestión de inventario.
 */
public class InventoryBusinessException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InventoryBusinessException(String message) {
		super(message);
	}

}
