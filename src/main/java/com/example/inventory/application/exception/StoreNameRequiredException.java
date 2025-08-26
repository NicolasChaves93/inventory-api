package com.example.inventory.application.exception;

public class StoreNameRequiredException extends RuntimeException {
    public StoreNameRequiredException() {
        super("El nombre de la tienda es obligatorio para esta operación.");
    }
}
