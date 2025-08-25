package com.example.inventory.interface_.v1;

import com.example.inventory.application.dto.ProductRequest;
import com.example.inventory.application.dto.ProductResponse;
import com.example.inventory.application.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductController.
 */
class ProductControllerTest {
    @Mock
    private ProductService productService;
    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe retornar producto por código")
    void debeRetornarProductoPorCodigo() {
        ProductResponse resp = mock(ProductResponse.class);
        when(productService.findByCode("P1")).thenReturn(resp);
        ResponseEntity<ProductResponse> response = controller.find("P1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe crear producto correctamente")
    void debeCrearProducto() {
        ProductRequest req = new ProductRequest();
        doNothing().when(productService).createProduct(req);
        ResponseEntity<Void> response = controller.create(req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe actualizar producto correctamente")
    void debeActualizarProducto() {
        ProductRequest req = new ProductRequest();
        doNothing().when(productService).updateProduct("P1", req);
        ResponseEntity<Void> response = controller.update("P1", req);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe eliminar producto correctamente")
    void debeEliminarProducto() {
        doNothing().when(productService).deleteProduct("P1");
        ResponseEntity<Void> response = controller.delete("P1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe listar todos los productos")
    void debeListarTodosLosProductos() {
        List<ProductResponse> lista = List.of(mock(ProductResponse.class));
        when(productService.findAll()).thenReturn(lista);
        ResponseEntity<List<ProductResponse>> response = controller.listAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }
}
