package com.example.inventory.interface_.v1;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.application.dto.ProductRequest;
import com.example.inventory.application.dto.ProductResponse;
import com.example.inventory.application.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de productos.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Productos", description = "Gestión de productos en inventario")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	// Endpoints
	
	@Operation(summary = "Buscar producto por código", description = "Devuelve un producto específico.")
	@GetMapping("/{code}")
	public ResponseEntity<ProductResponse> find(@PathVariable String code) {
		ProductResponse response = productService.findByCode(code);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el inventario.")
	@PostMapping
	public ResponseEntity<Void> create(@Valid @RequestBody ProductRequest request) {
		productService.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "Actualizar un producto", description = "Actualiza los datos de un producto existente.")
	@PutMapping("/{code}")
	public ResponseEntity<Void> update(@PathVariable String code, @Valid @RequestBody ProductRequest request) {
		productService.updateProduct(code, request);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Eliminar un producto", description = "Elimina un producto del inventario.")
	@DeleteMapping("/{code}")
	public ResponseEntity<Void> delete(@PathVariable String code) {
		
		productService.deleteProduct(code);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Listar todos los productos", description = "Devuelve una lista de todos los productos en el inventario.")
	@GetMapping
	public ResponseEntity<List<ProductResponse>> listAll() {
		return ResponseEntity.ok(productService.findAll());
	}
}
