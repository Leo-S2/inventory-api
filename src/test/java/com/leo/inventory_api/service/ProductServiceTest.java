package com.leo.inventory_api.service;

import com.leo.inventory_api.dto.CreateProductRequest;
import com.leo.inventory_api.dto.ProductResponse;
import com.leo.inventory_api.exception.ProductDuplicateNameException;
import com.leo.inventory_api.model.Product;
import com.leo.inventory_api.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void addProduct_ProductDuplication_Exception() {
        CreateProductRequest request = new CreateProductRequest("Teclado", 10, new BigDecimal("50"));

        when(productRepository.existsByName("Teclado")).thenReturn(true);

        assertThrows(ProductDuplicateNameException.class, () -> productService.addProduct(request));

        verify(productRepository, never()).save(any());
    }

    @Test
    void addProduct_newName_saveProduct() {
        CreateProductRequest request = new CreateProductRequest("Mouse", 5, new BigDecimal("30"));

        when(productRepository.existsByName("Mouse")).thenReturn(false);

        Product saveProduct = new Product();
        saveProduct.setId(1L);
        saveProduct.setName("Mouse");
        saveProduct.setStock(5);
        saveProduct.setPrice(new BigDecimal("30"));

        when(productRepository.save(any())).thenReturn(saveProduct);

        ProductResponse response = productService.addProduct(request);
        assertEquals("Mouse", response.name(), "Product name not equal to mouse");
        assertEquals(new BigDecimal(30), response.price(), "Product price not equal to 30");
        assertEquals(5, response.stock(), "Product stock not equal to 5");

        verify(productRepository).save(any());
    }
}
