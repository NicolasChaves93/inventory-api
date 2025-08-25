package com.example.inventory.interface_.v1;

import com.example.inventory.application.dto.StoreRequest;
import com.example.inventory.application.dto.StoreResponse;
import com.example.inventory.application.service.StoreService;
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
 * Pruebas unitarias para StoreController.
 */
class StoreControllerTest {
    @Mock
    private StoreService storeService;
    @InjectMocks
    private StoreController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe retornar tienda por nombre")
    void debeRetornarTiendaPorNombre() {
        StoreResponse resp = mock(StoreResponse.class);
        when(storeService.findByName("Tienda1")).thenReturn(resp);
        ResponseEntity<StoreResponse> response = controller.find("Tienda1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resp, response.getBody());
    }

    @Test
    @DisplayName("Debe retornar todas las tiendas")
    void debeRetornarTodasLasTiendas() {
        List<StoreResponse> lista = List.of(mock(StoreResponse.class));
        when(storeService.findAll()).thenReturn(lista);
        ResponseEntity<List<StoreResponse>> response = controller.findAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }

    @Test
    @DisplayName("Debe crear tienda correctamente")
    void debeCrearTienda() {
        StoreRequest req = new StoreRequest();
        doNothing().when(storeService).createStore(req);
        ResponseEntity<Void> response = controller.create(req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe actualizar tienda correctamente")
    void debeActualizarTienda() {
        StoreRequest req = new StoreRequest();
        doNothing().when(storeService).updateStore("Tienda1", req);
        ResponseEntity<Void> response = controller.update("Tienda1", req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe desactivar tienda correctamente")
    void debeDesactivarTienda() {
        doNothing().when(storeService).deactivateStore("Tienda1");
        ResponseEntity<Void> response = controller.deactivate("Tienda1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe activar tienda correctamente")
    void debeActivarTienda() {
        doNothing().when(storeService).activateStore("Tienda1");
        ResponseEntity<Void> response = controller.activate("Tienda1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Debe eliminar tienda correctamente")
    void debeEliminarTienda() {
        doNothing().when(storeService).deleteStore("Tienda1");
        ResponseEntity<Void> response = controller.delete("Tienda1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
