package com.eshop.library.service;

import com.eshop.library.dto.ProductDto;
import com.eshop.library.model.Product;
import com.eshop.library.repository.ProductRepository;
import com.eshop.library.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Product> products = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Optional<Product> result = Optional.ofNullable(productService.findById(productId));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testSave() {
        // Arrange
        ProductDto productDto = new ProductDto();
        MultipartFile imageProduct = null /* mock MultipartFile */;

        // Mock productRepository.save to return a non-null value
        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        // Act
        Product result = productService.save(imageProduct, productDto);

        // Assert
        assertNotNull(result);

    }
    }



