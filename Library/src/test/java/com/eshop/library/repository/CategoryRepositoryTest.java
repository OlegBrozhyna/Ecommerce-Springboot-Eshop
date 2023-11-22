package com.eshop.library.repository;

import com.eshop.library.dto.CategoryDto;
import com.eshop.library.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testUpdateCategory() {
        // Arrange
        // Creating a new category with the initial name "Test Category"
        Category category = new Category("Test Category");
        // Saving the category to the database and calling flush to ensure its persistence
        entityManager.persistAndFlush(category);

        // Act
        // Calling the update method, which returns the count of updated rows (expecting 1)
        int updatedRowCount = categoryRepository.update("Updated Category", category.getId());

        // Reload the category from the database to get the changes
        // Flushing and clearing the EntityManager to ensure changes are reflected in the database
        entityManager.flush();
        entityManager.clear();

        // Assert
        // Verifying that one row was updated
        assertThat(updatedRowCount).isEqualTo(1);

        // Verify the changes are persisted in the database
        // Retrieving the updated category from the database and checking its name
        Category retrievedCategory = categoryRepository.findById(category.getId()).orElse(null);
        assertThat(retrievedCategory).isNotNull();
        assertThat(retrievedCategory.getName()).isEqualTo("Updated Category");
    }

    @Test
    public void testFindAllByActivatedTrue() {
        // Arrange
        // Creating an active category with the name "Active Category"
        Category activeCategory = new Category("Active Category");
        // Setting the category as activated
        activeCategory.setActivated(true);
        // Saving the active category to the database and flushing to ensure its persistence
        entityManager.persistAndFlush(activeCategory);

        // Creating an inactive category with the name "Inactive Category"
        Category inactiveCategory = new Category("Inactive Category");
        // Setting the category as inactive
        inactiveCategory.setActivated(false);
        // Saving the inactive category to the database and flushing to ensure its persistence
        entityManager.persistAndFlush(inactiveCategory);

        // Act
        // Calling the repository method to find all activated categories
        List<Category> activeCategories = categoryRepository.findAllByActivatedTrue();

        // Assert
        // Verifying that the list of active categories is not empty
        assertThat(activeCategories).isNotEmpty();
        // Verifying that the list contains the active category
        assertThat(activeCategories).contains(activeCategory);
        // Verifying that the list does not contain the inactive category
        assertThat(activeCategories).doesNotContain(inactiveCategory);
    }


    @Test
    public void testGetCategoriesAndSize() {
        // Arrange
        // Creating two categories for testing
        Category category1 = new Category("Category 1");
        Category category2 = new Category("Category 2");

        // Saving categories to the database and flushing to ensure their persistence
        entityManager.persistAndFlush(category1);
        entityManager.persistAndFlush(category2);

        // Act
        // Calling the repository method to retrieve category information along with product size
        List<CategoryDto> categoryDtos = categoryRepository.getCategoriesAndSize();

        // Assert
        // Verifying that the list of category DTOs is not empty
        assertThat(categoryDtos).isNotEmpty();
        // Verifying that the list contains exactly 2 category DTOs
        assertThat(categoryDtos).hasSize(2);

        // Assuming there are 2 categories and no products associated
        // Verifying that the product size for the first category DTO is 0
        assertThat(categoryDtos.get(0).getProductSize()).isEqualTo(0);
        // Verifying that the product size for the second category DTO is 0
        assertThat(categoryDtos.get(1).getProductSize()).isEqualTo(0);
    }
}
