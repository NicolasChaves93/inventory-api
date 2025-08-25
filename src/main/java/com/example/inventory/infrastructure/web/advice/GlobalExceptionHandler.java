package com.example.inventory.infrastructure.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.inventory.application.dto.ApiError;
import com.example.inventory.application.exception.DuplicateCodeException;
import com.example.inventory.application.exception.DuplicateNameException;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.exception.InventoryAlreadyExistsException;
import com.example.inventory.application.exception.InventoryBusinessException;
import com.example.inventory.application.exception.ProductNotFoundException;
import com.example.inventory.application.exception.StoreNotFoundException;

/**
 * Manejador global de excepciones para la API REST.
 * Intercepta excepciones y las convierte en respuestas HTTP adecuadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Helper centralizado
    private ResponseEntity<ApiError> buildErrorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new ApiError(status.value(), message), status);
    }

    // ----- Excepciones de inventario -----
    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleInventoryAlreadyExists(InventoryAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InventoryBusinessException.class)
    public ResponseEntity<ApiError> handleInventoryBusiness(InventoryBusinessException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ----- Excepciones de tienda -----
    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<ApiError> handleStoreNotFound(StoreNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateNameException.class)
    public ResponseEntity<ApiError> handleDuplicateName(DuplicateNameException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ----- Excepciones de producto -----
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiError> handleProductNotFound(ProductNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateCodeException.class)
    public ResponseEntity<ApiError> handleDuplicateCode(DuplicateCodeException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // ----- Excepción genérica de entidades -----
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ----- Fallback genérico -----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }
}
