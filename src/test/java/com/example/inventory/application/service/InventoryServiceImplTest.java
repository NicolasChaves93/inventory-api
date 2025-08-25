package com.example.inventory.application.service;

import com.example.inventory.application.dto.InventoryListProductResponse;
import com.example.inventory.application.dto.InventoryResponse;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.application.port.out.EventPublisherPort;
import com.example.inventory.application.service.impl.InventoryServiceImpl;
import com.example.inventory.domain.model.Inventory;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.model.Store;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.ProductRepository;
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
 * Pruebas unitarias para InventoryServiceImpl.
 * Se cubren casos exitosos y de error, siguiendo buenas prácticas y Clean Code.
 */
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("findByStore")
    class FindByStore {
        @Test
        @DisplayName("Debe retornar productos de una tienda existente")
        void debeRetornarProductosDeTiendaExistente() {
            String storeName = "Tienda1";
            Store store = new Store(storeName, "Calle 123", true);
            Inventory inv = mock(Inventory.class);
            Product producto = new Product("P1", "Producto 1", "desc", 100.0f);
            when(inv.getProduct()).thenReturn(producto);
            when(inv.getQuantity()).thenReturn(10);
            when(storeRepository.findByName(storeName)).thenReturn(Optional.of(store));
            when(inventoryRepository.findByStoreName(storeName)).thenReturn(List.of(inv));

            InventoryListProductResponse response = inventoryService.findByStore(storeName);

            assertEquals(storeName, response.getStoreName());
            assertEquals(1, response.getItems().size());
            assertEquals("P1", response.getItems().get(0).getProductCode());
            assertEquals(10, response.getItems().get(0).getQuantity());
        }

        @Test
        @DisplayName("Debe lanzar excepción si la tienda no existe")
        void debeLanzarExcepcionSiTiendaNoExiste() {
            when(storeRepository.findByName("Inexistente")).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> inventoryService.findByStore("Inexistente"));
        }
    }

    @Nested
    @DisplayName("addStock")
    class AddStock {
        @Test
        @DisplayName("Debe agregar stock correctamente")
        void debeAgregarStockCorrectamente() {
            String store = "Tienda1", product = "P1";
            Inventory inv = mock(Inventory.class);
            when(inv.getQuantity()).thenReturn(5);
            when(inventoryRepository.findByStoreNameAndProductCode(store, product)).thenReturn(Optional.of(inv));
            InventoryResponse response = inventoryService.addStock(store, product, 3);
            assertEquals(8, response.getQuantity());
            verify(eventPublisher).publish(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción si cantidad es menor o igual a cero")
        void debeLanzarExcepcionSiCantidadNoValida() {
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.of(mock(Inventory.class)));
            assertThrows(IllegalArgumentException.class, () -> inventoryService.addStock("Tienda1", "P1", 0));
        }
    }

    @Nested
    @DisplayName("removeStock")
    class RemoveStock {
        @Test
        @DisplayName("Debe remover stock correctamente")
        void debeRemoverStockCorrectamente() {
            String store = "Tienda1", product = "P1";
            Inventory inv = mock(Inventory.class);
            when(inv.getQuantity()).thenReturn(10);
            when(inventoryRepository.findByStoreNameAndProductCode(store, product)).thenReturn(Optional.of(inv));
            InventoryResponse response = inventoryService.removeStock(store, product, 5);
            assertEquals(5, response.getQuantity());
            verify(eventPublisher).publish(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción si cantidad es menor o igual a cero")
        void debeLanzarExcepcionSiCantidadNoValida() {
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.of(mock(Inventory.class)));
            assertThrows(IllegalArgumentException.class, () -> inventoryService.removeStock("Tienda1", "P1", 0));
        }

        @Test
        @DisplayName("Debe lanzar excepción si stock insuficiente")
        void debeLanzarExcepcionSiStockInsuficiente() {
            Inventory inv = mock(Inventory.class);
            when(inv.getQuantity()).thenReturn(2);
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.of(inv));
            assertThrows(IllegalArgumentException.class, () -> inventoryService.removeStock("Tienda1", "P1", 5));
        }
    }

    @Nested
    @DisplayName("getStock")
    class GetStock {
        @Test
        @DisplayName("Debe retornar el stock actual")
        void debeRetornarStockActual() {
            Inventory inv = mock(Inventory.class);
            when(inv.getQuantity()).thenReturn(7);
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.of(inv));
            int stock = inventoryService.getStock("Tienda1", "P1");
            assertEquals(7, stock);
        }

        @Test
        @DisplayName("Debe lanzar excepción si inventario no existe")
        void debeLanzarExcepcionSiInventarioNoExiste() {
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> inventoryService.getStock("Tienda1", "P1"));
        }
    }

    @Nested
    @DisplayName("createInventory")
    class CreateInventory {
        @Test
        @DisplayName("Debe crear inventario correctamente")
        void debeCrearInventarioCorrectamente() {
            String store = "Tienda1", product = "P1";
            when(storeRepository.findByName(store)).thenReturn(Optional.of(new Store(store, "Calle 123", true)));
            when(productRepository.findByCode(product)).thenReturn(Optional.of(new Product(product, "Producto 1", "desc", 100.0f)));
            when(inventoryRepository.findByStoreNameAndProductCode(store, product)).thenReturn(Optional.empty());
            InventoryResponse response = inventoryService.createInventory(store, product, 10);
            assertEquals(store, response.getStoreName());
            assertEquals(product, response.getProductCode());
            assertEquals(10, response.getQuantity());
            verify(eventPublisher).publish(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción si inventario ya existe")
        void debeLanzarExcepcionSiInventarioYaExiste() {
            when(storeRepository.findByName(any())).thenReturn(Optional.of(new Store("Tienda1", "Calle 123", true)));
            when(productRepository.findByCode(any())).thenReturn(Optional.of(new Product("P1", "Producto 1", "desc", 100.0f)));
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.of(mock(Inventory.class)));
            assertThrows(Exception.class, () -> inventoryService.createInventory("Tienda1", "P1", 5));
        }

        @Test
        @DisplayName("Debe lanzar excepción si stock inicial es negativo")
        void debeLanzarExcepcionSiStockInicialNegativo() {
            when(storeRepository.findByName(any())).thenReturn(Optional.of(new Store("Tienda1", "Calle 123", true)));
            when(productRepository.findByCode(any())).thenReturn(Optional.of(new Product("P1", "Producto 1", "desc", 100.0f)));
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> inventoryService.createInventory("Tienda1", "P1", -1));
        }
    }

    @Nested
    @DisplayName("deleteInventory")
    class DeleteInventory {
        @Test
        @DisplayName("Debe eliminar inventario correctamente")
        void debeEliminarInventarioCorrectamente() {
            Inventory inv = mock(Inventory.class);
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.of(inv));
            assertDoesNotThrow(() -> inventoryService.deleteInventory("Tienda1", "P1"));
            verify(eventPublisher).publish(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción si inventario no existe")
        void debeLanzarExcepcionSiInventarioNoExiste() {
            when(inventoryRepository.findByStoreNameAndProductCode(any(), any())).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> inventoryService.deleteInventory("Tienda1", "P1"));
        }
    }
}
