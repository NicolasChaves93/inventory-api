package com.example.inventory.interface_.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.application.dto.InventoryListProductResponse;
import com.example.inventory.application.dto.InventoryListStoreResponse;
import com.example.inventory.application.dto.InventoryRequest;
import com.example.inventory.application.dto.InventoryResponse;
import com.example.inventory.application.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión del inventario.
 */
@RestController
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventario", description = "Gestión del inventario")
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	public InventoryController(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}
	
	// Endpoints
	
	@Operation(summary = "Consultar stock de un producto en una tienda", description = "Devuelve la cantidad de stock disponible de un producto en una tienda específica.")
	@GetMapping("/stock/{storeName}/{productCode}")
    public ResponseEntity<Integer> getStock(@PathVariable String storeName, @PathVariable String productCode) {
        int stock = inventoryService.getStock(storeName, productCode);
        return ResponseEntity.ok(stock);
    }
	
	@Operation(summary = "Consultar productos en una tienda", description = "Devuelve la lista de productos disponibles en una tienda específica.")
	@GetMapping("/products/{storeName}")
	public ResponseEntity<InventoryListProductResponse> getProductsByStore(@PathVariable String storeName) {
        var response = inventoryService.findByStore(storeName);
        return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Consultar tiendas con un producto", description = "Devuelve la lista de tiendas que tienen un producto específico en stock.")
	@GetMapping("/stores/{productCode}")
	public ResponseEntity<InventoryListStoreResponse> getStoresByProduct(@PathVariable String productCode) {
        var response = inventoryService.findByProduct(productCode);
        return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "Añadir stock a un producto en una tienda", description = "Incrementa la cantidad de stock de un producto en una tienda específica.")
	@PostMapping("/add")
	public ResponseEntity<InventoryResponse> addStock(@Valid @RequestBody InventoryRequest request) {
	    InventoryResponse response = inventoryService.addStock(
	        request.getStoreName(),
	        request.getProductCode(),
	        request.getQuantity()
	    );
	    return ResponseEntity.ok(response);
	}

	@Operation(summary = "Quitar stock de un producto en una tienda", description = "Decrementa la cantidad de stock de un producto en una tienda específica.")
	@PostMapping("/remove")
	public ResponseEntity<InventoryResponse> removeStock(@Valid @RequestBody InventoryRequest request) {
	    InventoryResponse response = inventoryService.removeStock(
	        request.getStoreName(),
	        request.getProductCode(),
	        request.getQuantity()
	    );
	    return ResponseEntity.ok(response);
	}
	
	@Operation(
		    summary = "Crear un nuevo inventario",
		    description = "Crea un registro de inventario para un producto en una tienda específica con stock inicial."
	)
	@ApiResponses({
	    @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente"),
	    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
	    @ApiResponse(responseCode = "404", description = "Tienda o producto no encontrados"),
	    @ApiResponse(responseCode = "409", description = "El inventario ya existe")
	})
	@PostMapping
	public ResponseEntity<InventoryResponse> createInventory(@Valid @RequestBody InventoryRequest request) {
	    var response = inventoryService.createInventory(
	        request.getStoreName(),
	        request.getProductCode(),
	        request.getQuantity()
	    );
	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Operation(summary = "Eliminar inventario de un producto en una tienda", description = "Elimina el registro de inventario de un producto en una tienda específica.")
	@DeleteMapping("/{storeName}/{productCode}")
    public ResponseEntity<Void> deleteInventory(@PathVariable String storeName, @PathVariable String productCode) {
        inventoryService.deleteInventory(storeName, productCode);
        return ResponseEntity.noContent().build();
    }
}
