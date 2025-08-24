package com.example.inventory.interface_.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "Tiendas", description = "Gestión de tiendas en inventario")
public class StoreController {
	
	private final StoreService storeService;
	
	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}
	
	// Endpoints
	
	@Operation(summary = "Buscar tienda por nombre", description = "Devuelve una tienda específica.")
	@GetMapping("/{name}")
	public ResponseEntity<StoreResponse> find(@PathVariable String name) {
		StoreResponse response = storeService.findByName(name);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Listar todas las tiendas", description = "Devuelve una lista de todas las tiendas.")
	@GetMapping
	public ResponseEntity<List<StoreResponse>> findAll() {
		List<StoreResponse> response = storeService.findAll();
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Crear una nueva tienda", description = "Crea una nueva tienda en el inventario.")
	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody StoreRequest request) {
		// Implementación del endpoint para crear una tienda
		storeService.createStore(request);
		return ResponseEntity.status(201).build();
	}
	
	@Operation(summary = "Actualizar una tienda", description = "Actualiza los datos de una tienda existente.")
	@PutMapping("/{name}")
	public ResponseEntity<Void> update(@PathVariable String name, @Valid @RequestBody StoreRequest request) {
		// Implementación del endpoint para actualizar una tienda
		storeService.updateStore(name, request);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Desactivar una tienda", description = "Desactiva una tienda específica.")
	@PutMapping("/deactivate/{name}")
	public ResponseEntity<Void> deactivate(@PathVariable String name) {
		storeService.deactivateStore(name);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Activar una tienda", description = "Activa una tienda específica.")
	@PutMapping("/activate/{name}")
	public ResponseEntity<Void> activate(@PathVariable String name) {
		storeService.activateStore(name);
		return ResponseEntity.ok().build();
	}
	
	@Operation(summary = "Eliminar una tienda", description = "Elimina una tienda específica.")
	@DeleteMapping("/{name}")
	public ResponseEntity<Void> delete(@PathVariable String name) {
		storeService.deleteStore(name);
		return ResponseEntity.noContent().build();
	}
}
