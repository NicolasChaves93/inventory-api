package com.example.inventory.application.service.impl;

import com.example.inventory.application.dto.ProductRequest;
import com.example.inventory.application.dto.ProductResponse;
import com.example.inventory.application.exception.DuplicateCodeException;
import com.example.inventory.application.exception.EntityNotFoundException;
import com.example.inventory.domain.model.Product;
import com.example.inventory.domain.repository.ProductRepository;
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
 * Pruebas unitarias para ProductServiceImpl.
 */
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("findByCode")
    class FindByCode {
        @Test
        @DisplayName("Debe retornar producto existente")
        void debeRetornarProductoExistente() {
            Product producto = new Product("P1", "Nombre", "desc", 10.0f);
            when(productRepository.findByCode("P1")).thenReturn(Optional.of(producto));
            ProductResponse response = productService.findByCode("P1");
            assertEquals("P1", response.getCode());
            assertEquals("Nombre", response.getName());
        }

        @Test
        @DisplayName("Debe lanzar excepción si producto no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(productRepository.findByCode("P2")).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> productService.findByCode("P2"));
        }
    }

    @Nested
    @DisplayName("createProduct")
    class CreateProduct {
        @Test
        @DisplayName("Debe crear producto correctamente")
        void debeCrearProductoCorrectamente() {
            ProductRequest req = new ProductRequest();
            req.setCode("P3");
            req.setName("Nuevo");
            req.setDescription("desc");
            req.setPrice(20.0f);
            when(productRepository.findByCode("P3")).thenReturn(Optional.empty());
            productService.createProduct(req);
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción si código ya existe")
        void debeLanzarExcepcionSiCodigoYaExiste() {
            when(productRepository.findByCode("P3")).thenReturn(Optional.of(mock(Product.class)));
            ProductRequest req = new ProductRequest();
            req.setCode("P3");
            req.setName("Nuevo");
            req.setDescription("desc");
            req.setPrice(20.0f);
            assertThrows(DuplicateCodeException.class, () -> productService.createProduct(req));
        }
    }

    @Nested
    @DisplayName("updateProduct")
    class UpdateProduct {
        @Test
        @DisplayName("Debe actualizar producto existente")
        void debeActualizarProductoExistente() {
            Product producto = new Product("P4", "Nombre", "desc", 10.0f);
            when(productRepository.findByCode("P4")).thenReturn(Optional.of(producto));
            ProductRequest req = new ProductRequest();
            req.setCode("P4");
            req.setName("Actualizado");
            req.setDescription("nueva desc");
            req.setPrice(30.0f);
            productService.updateProduct("P4", req);
            verify(productRepository).save(producto);
            assertEquals("Actualizado", producto.getName());
            assertEquals("nueva desc", producto.getDescription());
            assertEquals(30.0f, producto.getPrice());
        }

        @Test
        @DisplayName("Debe lanzar excepción si producto no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(productRepository.findByCode("P5")).thenReturn(Optional.empty());
            ProductRequest req = new ProductRequest();
            req.setCode("P5");
            req.setName("Nombre");
            req.setDescription("desc");
            req.setPrice(10.0f);
            assertThrows(EntityNotFoundException.class, () -> productService.updateProduct("P5", req));
        }
    }

    @Nested
    @DisplayName("deleteProduct")
    class DeleteProduct {
        @Test
        @DisplayName("Debe eliminar producto existente")
        void debeEliminarProductoExistente() {
            Product producto = new Product("P6", "Nombre", "desc", 10.0f);
            when(productRepository.findByCode("P6")).thenReturn(Optional.of(producto));
            productService.deleteProduct("P6");
            verify(productRepository).delete(producto);
        }

        @Test
        @DisplayName("Debe lanzar excepción si producto no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(productRepository.findByCode("P7")).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct("P7"));
        }
    }

    @Test
    @DisplayName("Debe retornar todos los productos")
    void debeRetornarTodosLosProductos() {
        Product p1 = new Product("P1", "Nombre1", "desc1", 10.0f);
        Product p2 = new Product("P2", "Nombre2", "desc2", 20.0f);
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));
        List<ProductResponse> productos = productService.findAll();
        assertEquals(2, productos.size());
        assertEquals("P1", productos.get(0).getCode());
        assertEquals("P2", productos.get(1).getCode());
    }
}
