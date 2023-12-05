package com.eshop.library.service;

import static org.junit.jupiter.api.Assertions.*;

import com.eshop.library.model.Category;
import com.eshop.library.repository.CategoryRepository;
import com.eshop.library.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void testSaveCategory() {
        // Arrange
        Category categoryToSave = new Category("Test Category");

        // Mock the behavior of the categoryRepository
        Mockito.when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Category savedCategory = categoryService.save(categoryToSave);

        // Assert
        assertEquals(categoryToSave.getName(), savedCategory.getName());
    }

    @Test
    public void testFindAllByActivatedTrue() {
        // Arrange
        Category activatedCategory = new Category("Activated Category");
        activatedCategory.setActivated(true);
        Category deactivatedCategory = new Category("Deactivated Category");
        deactivatedCategory.setActivated(false);

        Mockito.when(categoryRepository.findAllByActivatedTrue())
                .thenReturn(Arrays.asList(activatedCategory, deactivatedCategory));

        // Act
        List<Category> activatedCategories = categoryService.findAllByActivatedTrue();

        // Assert
        assertEquals(2, activatedCategories.size());
        assertTrue(activatedCategories.get(0).isActivated());
    }

}
