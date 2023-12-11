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

public class ProductServiceTest {

    // Mocking the ProductRepository for testing purposes.
    @Mock
    private ProductRepository productRepository;

    // Injecting mocks into the ProductServiceImpl for testing.
    @InjectMocks
    private ProductServiceImpl productService;

    // A ProductDto instance for testing.
    private ProductDto productDto;

    // Setting up the mocks before each test.
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Testing the findAll method of ProductService.
    @Test
    void testFindAll() {
        // Arrange
        // Creating an empty list of products and configuring the behavior of productRepository.
        List<Product> products = new ArrayList<>();
        when(productRepository.findAll()).thenReturn(products);

        // Act
        // Invoking the findAll method and capturing the result.
        List<Product> result = productService.findAll();

        // Assert
        // Verifying that the result is not null, equal to the expected list, and that findAll was called once.
        assertNotNull(result);
        assertEquals(products, result);
        verify(productRepository, times(1)).findAll();
    }

    // Testing the findById method of ProductService.
    @Test
    void testFindById() {
        // Arrange
        // Providing a sample productId and a sample product, and configuring the behavior of productRepository.
        Long productId = 1L;
        Product product = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        // Invoking the findById method and capturing the result.
        Optional<Product> result = Optional.ofNullable(productService.findById(productId));

        // Assert
        // Verifying that the result is present, equal to the expected product, and that findById was called once.
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testSave() {
        // Arrange
        // Creating a sample ProductDto and a mock MultipartFile.
        ProductDto productDto = new ProductDto();
        MultipartFile imageProduct = null /* mock MultipartFile */;

        // Mocking the behavior of productRepository.save to return a non-null value.
        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        // Act
        // Invoking the save method and capturing the result.
        Product result = productService.save(imageProduct, productDto);

        // Assert
        // Verifying that the result is not null.
        assertNotNull(result);
    }
}


