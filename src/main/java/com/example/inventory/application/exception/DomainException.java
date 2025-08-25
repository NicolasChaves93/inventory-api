package com.example.inventory.application.exception;

/**
 * Excepcion base para errores de dominio.
 */
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DomainException(String message) {
		super(message);
	}

}
