package com.example.inventory.application.exception;

public class DuplicateCodeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DuplicateCodeException(String code) {
		super("El código ya existe: " + code);
	}

}
