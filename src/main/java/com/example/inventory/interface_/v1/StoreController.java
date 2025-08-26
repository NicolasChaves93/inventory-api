package com.example.inventory.interface_.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.application.dto.StoreRequest;
import com.example.inventory.application.dto.StoreResponse;
import com.example.inventory.application.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de tiendas.
 * Expone endpoints para operaciones CRUD y activación/desactivación.
 */

import com.example.inventory.config.StoreProperties;

@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "Tiendas", description = "Gestión de tiendas en inventario")

public class StoreController {
	private final StoreService storeService;
	private final StoreProperties storeProperties;

	public StoreController(StoreService storeService, StoreProperties storeProperties) {
		this.storeService = storeService;
		this.storeProperties = storeProperties;
	}
	
	// ---------- READ ----------
	@Operation(summary = "Buscar tienda por nombre", description = "Devuelve una tienda específica.")
	@GetMapping
	public ResponseEntity<StoreResponse> find(@RequestParam(required = false) String name) {
		String storeName = storeProperties.isLocal() ? storeProperties.getName() : name;
		StoreResponse response = storeService.findByName(storeName);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Listar todas las tiendas", description = "Devuelve una lista de todas las tiendas.")
	@GetMapping("/all")
	public ResponseEntity<List<StoreResponse>> findAll() {
		List<StoreResponse> response = storeService.findAll();
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Crear una nueva tienda", description = "Crea una nueva tienda en el inventario.")
	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody StoreRequest request) {
		if (storeProperties.isLocal()) {
			request.setName(storeProperties.getName());
		}
		storeService.createStore(request);
		return ResponseEntity.status(201).build();
	}
	
	@Operation(summary = "Actualizar una tienda", description = "Actualiza los datos de una tienda existente.")
	@PutMapping
	public ResponseEntity<Void> update(@RequestParam(required = false) String name, @Valid @RequestBody StoreRequest request) {
		String storeName = storeProperties.isLocal() ? storeProperties.getName() : name;
		storeService.updateStore(storeName, request);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Desactivar una tienda", description = "Desactiva una tienda específica.")
	@PutMapping("/deactivate")
	public ResponseEntity<Void> deactivate(@RequestParam(required = false) String name) {
		String storeName = storeProperties.isLocal() ? storeProperties.getName() : name;
	    storeService.deactivateStore(storeName);
	    return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Activar una tienda", description = "Activa una tienda específica.")
	@PutMapping("/activate")
	public ResponseEntity<Void> activate(@RequestParam(required = false) String name) {
		String storeName = storeProperties.isLocal() ? storeProperties.getName() : name;
		storeService.activateStore(storeName);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Eliminar una tienda", description = "Elimina una tienda específica.")
	@DeleteMapping
	public ResponseEntity<Void> delete(@RequestParam(required = false) String name) {
		String storeName = storeProperties.isLocal() ? storeProperties.getName() : name;
		storeService.deleteStore(storeName);
		return ResponseEntity.noContent().build();
	}
}
