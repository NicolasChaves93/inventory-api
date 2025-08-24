package com.example.inventory.application.exception;

/**
 * 
 */
public class DuplicateNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicateNameException(String name) {
		super("El nombre ya existe: " + name);
	}

}
