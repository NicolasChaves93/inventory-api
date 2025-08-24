package com.example.inventory.infrastructure.web.advice;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.inventory.application.exception.DuplicateCodeException;
import com.example.inventory.application.exception.DuplicateNameException;
import com.example.inventory.application.exception.EntityNotFoundException;

/**
 * Manejador global de excepciones para la API REST.
 * Intercepta excepciones y las convierte en respuestas HTTP adecuadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DuplicateNameException.class)
	public ResponseEntity<?> handleConflict(DuplicateNameException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(Map.of("status", 409, "message", ex.getMessage()));
	}
	
	
	@ExceptionHandler(DuplicateCodeException.class)
	public ResponseEntity<?> handleConflict(DuplicateCodeException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(Map.of("status", 409, "message", ex.getMessage()));
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("status", 404, "message", ex.getMessage()));
    }
	
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
