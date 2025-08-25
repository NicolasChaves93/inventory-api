package com.example.inventory.application.service.impl;

import com.example.inventory.application.dto.StoreRequest;
import com.example.inventory.application.dto.StoreResponse;
import com.example.inventory.application.exception.DuplicateNameException;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para StoreServiceImpl.
 */
class StoreServiceImplTest {
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private StoreServiceImpl storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Debe retornar todas las tiendas")
    void debeRetornarTodasLasTiendas() {
        Store s1 = new Store("Tienda1", "Calle 1", true);
        Store s2 = new Store("Tienda2", "Calle 2", false);
        when(storeRepository.findAll()).thenReturn(List.of(s1, s2));
        List<StoreResponse> tiendas = storeService.findAll();
        assertEquals(2, tiendas.size());
        assertEquals("Tienda1", tiendas.get(0).getName());
        assertEquals("Tienda2", tiendas.get(1).getName());
    }

    @Nested
    @DisplayName("createStore")
    class CreateStore {
        @Test
        @DisplayName("Debe crear tienda correctamente")
        void debeCrearTiendaCorrectamente() {
            StoreRequest req = new StoreRequest();
            req.setName("TiendaNueva");
            req.setAddress("Calle Nueva");
            when(storeRepository.findByName("TiendaNueva")).thenReturn(Optional.empty());
            storeService.createStore(req);
            verify(storeRepository).save(any(Store.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si nombre ya existe")
        void debeLanzarExcepcionSiNombreYaExiste() {
            when(storeRepository.findByName("TiendaNueva")).thenReturn(Optional.of(mock(Store.class)));
            StoreRequest req = new StoreRequest();
            req.setName("TiendaNueva");
            req.setAddress("Calle Nueva");
            assertThrows(DuplicateNameException.class, () -> storeService.createStore(req));
        }
    }

    @Nested
    @DisplayName("findByName")
    class FindByName {
        @Test
        @DisplayName("Debe retornar tienda existente")
        void debeRetornarTiendaExistente() {
            Store s = new Store("Tienda1", "Calle 1", true);
            when(storeRepository.findByName("Tienda1")).thenReturn(Optional.of(s));
            StoreResponse resp = storeService.findByName("Tienda1");
            assertEquals("Tienda1", resp.getName());
            assertEquals("Calle 1", resp.getAddress());
            assertTrue(resp.isStatus());
        }

        @Test
        @DisplayName("Debe lanzar excepción si tienda no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(storeRepository.findByName("Inexistente")).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> storeService.findByName("Inexistente"));
        }
    }

    @Nested
    @DisplayName("updateStore")
    class UpdateStore {
        @Test
        @DisplayName("Debe actualizar tienda existente")
        void debeActualizarTiendaExistente() {
            Store s = new Store("Tienda1", "Calle 1", true);
            when(storeRepository.findByName("Tienda1")).thenReturn(Optional.of(s));
            StoreRequest req = new StoreRequest();
            req.setName("Tienda1");
            req.setAddress("Calle Actualizada");
            when(storeRepository.findByName("Tienda1")).thenReturn(Optional.of(s));
            storeService.updateStore("Tienda1", req);
            verify(storeRepository).save(any(Store.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si tienda no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(storeRepository.findByName("Inexistente")).thenReturn(Optional.empty());
            StoreRequest req = new StoreRequest();
            req.setName("Inexistente");
            req.setAddress("Calle Nueva");
            assertThrows(EntityNotFoundException.class, () -> storeService.updateStore("Inexistente", req));
        }
    }

    @Nested
    @DisplayName("deactivateStore y activateStore")
    class ActivateDeactivateStore {
        @Test
        @DisplayName("Debe desactivar tienda activa")
        void debeDesactivarTiendaActiva() {
            Store s = new Store("Tienda1", "Calle 1", true);
            when(storeRepository.findByName("Tienda1")).thenReturn(Optional.of(s));
            storeService.deactivateStore("Tienda1");
            verify(storeRepository).save(s);
            assertFalse(s.isStatus());
        }

        @Test
        @DisplayName("Debe activar tienda inactiva")
        void debeActivarTiendaInactiva() {
            Store s = new Store("Tienda1", "Calle 1", false);
            when(storeRepository.findByName("Tienda1")).thenReturn(Optional.of(s));
            storeService.activateStore("Tienda1");
            verify(storeRepository).save(s);
            assertTrue(s.isStatus());
        }
    }

    @Nested
    @DisplayName("deleteStore")
    class DeleteStore {
        @Test
        @DisplayName("Debe eliminar tienda existente")
        void debeEliminarTiendaExistente() {
            Store s = new Store("Tienda1", "Calle 1", true);
            when(storeRepository.findByName("Tienda1")).thenReturn(Optional.of(s));
            storeService.deleteStore("Tienda1");
            verify(storeRepository).delete(s);
        }

        @Test
        @DisplayName("Debe lanzar excepción si tienda no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(storeRepository.findByName("Inexistente")).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> storeService.deleteStore("Inexistente"));
        }
    }
}
