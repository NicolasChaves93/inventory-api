package com.example.inventory.interface_.v1;

import com.example.inventory.application.dto.InventoryListProductResponse;
import com.example.inventory.application.dto.InventoryListStoreResponse;
import com.example.inventory.application.dto.InventoryRequest;
import com.example.inventory.application.dto.InventoryResponse;
import com.example.inventory.application.service.InventoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para InventoryController.
 */
class InventoryControllerTest {
    @Mock
    private InventoryService inventoryService;
    @InjectMocks
    private InventoryController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe retornar el stock de un producto en una tienda")
    void debeRetornarStock() {
        when(inventoryService.getStock("Tienda1", "P1")).thenReturn(10);
        ResponseEntity<Integer> response = controller.getStock("Tienda1", "P1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody());
    }

    @Test
    @DisplayName("Debe retornar productos por tienda")
    void debeRetornarProductosPorTienda() {
        InventoryListProductResponse resp = mock(InventoryListProductResponse.class);
        when(inventoryService.findByStore("Tienda1")).thenReturn(resp);
        ResponseEntity<InventoryListProductResponse> response = controller.getProductsByStore("Tienda1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe retornar tiendas por producto")
    void debeRetornarTiendasPorProducto() {
        InventoryListStoreResponse resp = mock(InventoryListStoreResponse.class);
        when(inventoryService.findByProduct("P1")).thenReturn(resp);
        ResponseEntity<InventoryListStoreResponse> response = controller.getStoresByProduct("P1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe agregar stock correctamente")
    void debeAgregarStock() {
        InventoryRequest req = new InventoryRequest();
        req.setStoreName("Tienda1");
        req.setProductCode("P1");
        req.setQuantity(5);
        InventoryResponse resp = mock(InventoryResponse.class);
        when(inventoryService.addStock("Tienda1", "P1", 5)).thenReturn(resp);
        ResponseEntity<InventoryResponse> response = controller.addStock(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe remover stock correctamente")
    void debeRemoverStock() {
        InventoryRequest req = new InventoryRequest();
        req.setStoreName("Tienda1");
        req.setProductCode("P1");
        req.setQuantity(2);
        InventoryResponse resp = mock(InventoryResponse.class);
        when(inventoryService.removeStock("Tienda1", "P1", 2)).thenReturn(resp);
        ResponseEntity<InventoryResponse> response = controller.removeStock(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe crear inventario correctamente")
    void debeCrearInventario() {
        InventoryRequest req = new InventoryRequest();
        req.setStoreName("Tienda1");
        req.setProductCode("P1");
        req.setQuantity(10);
        InventoryResponse resp = mock(InventoryResponse.class);
        when(inventoryService.createInventory("Tienda1", "P1", 10)).thenReturn(resp);
        ResponseEntity<InventoryResponse> response = controller.createInventory(req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe eliminar inventario correctamente")
    void debeEliminarInventario() {
        doNothing().when(inventoryService).deleteInventory("Tienda1", "P1");
        ResponseEntity<Void> response = controller.deleteInventory("Tienda1", "P1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
