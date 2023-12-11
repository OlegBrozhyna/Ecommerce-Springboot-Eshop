package com.eshop.library.service;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.library.model.Category;
import com.eshop.library.repository.CategoryRepository;
import com.eshop.library.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTest {

    // Mocking the CategoryRepository for testing purposes.
    @Mock
    private CategoryRepository categoryRepository;
    // Injecting mocks into the CategoryServiceImpl for testing.
    @InjectMocks
    private CategoryServiceImpl categoryService;

    // Testing the save method of CategoryServiceImpl.
    @Test
    public void testSaveCategory() {
        // Arrange
        // Creating a sample Category to be saved.
        Category categoryToSave = new Category("Test Category");

        // Mocking the behavior of the categoryRepository save method.
        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        // Invoking the save method and capturing the result.
        Category savedCategory = categoryService.save(categoryToSave);

        // Assert
        // Verifying that the savedCategory matches the expected categoryToSave.
        assertEquals(categoryToSave.getName(), savedCategory.getName());
    }

    @Test
    public void testFindAllByActivatedTrue() {
        // Arrange
        // Creating activated and deactivated sample categories.
        Category activatedCategory = new Category("Activated Category");
        activatedCategory.setActivated(true);
        Category deactivatedCategory = new Category("Deactivated Category");
        deactivatedCategory.setActivated(false);

        // Configuring the behavior of categoryRepository for findAllByActivatedTrue.
        Mockito.when(categoryRepository.findAllByActivatedTrue())
                .thenReturn(Arrays.asList(activatedCategory, deactivatedCategory));

        // Act
        // Invoking findAllByActivatedTrue method and capturing the result.
        List<Category> activatedCategories = categoryService.findAllByActivatedTrue();

        // Assert
        // Verifying that two categories are returned and the first one is activated.
        assertEquals(2, activatedCategories.size());
        assertTrue(activatedCategories.get(0).isActivated());
    }
}