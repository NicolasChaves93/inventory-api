package com.example.inventory.application.exception;

/**
 * Excepción genénerica para entidades no encontradas.
 * Reutilizable para diferentes tipos de entidades.
 */
public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String entity, String identifier) {
		super(entity + " no econtrado: " + identifier);
	}

}
