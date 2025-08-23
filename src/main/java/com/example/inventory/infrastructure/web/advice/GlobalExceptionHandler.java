package com.example.inventory.infrastructure.web.advice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para la API REST.
 * Intercepta excepciones y las convierte en respuestas HTTP adecuadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGeneric(Exception ex) {
		Map<String, Object> body = Map.of(
				"timestamp", Instant.now().toString(),
				"status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"message", "Error interno del servidor"
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		
	}

}
