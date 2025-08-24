package com.example.inventory.application.service;

import java.util.List;

import com.example.inventory.application.dto.StoreRequest;
import com.example.inventory.application.dto.StoreResponse;

/**
 * Caso de uso para la gestión de tiendas. Orquesta puertos y lógica de negocio.
 */
public interface StoreService {
	
	StoreResponse findByName(String name);

    List<StoreResponse> findAll();

    void createStore(StoreRequest request);

    void updateStore(String name, StoreRequest request);

    void deactivateStore(String name);

    void activateStore(String name);

    void deleteStore(String name);

}
