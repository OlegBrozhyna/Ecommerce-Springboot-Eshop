package com.eshop.library.repository;

import com.eshop.library.model.Category;
import com.eshop.library.model.Product;
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
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void testGetAllProduct() {
        // Arrange
        // Insert some test products into the database
        Product product1 = new Product(); // Create the first test product
        Product product2 = new Product(); // Create the second test product
        testEntityManager.persistAndFlush(product1); // Save and flush the first product to the database
        testEntityManager.persistAndFlush(product2); // Save and flush the second product to the database

        // Act
        Object result = productRepository.getAllProduct(); // Call the getAllProduct method

        // Assert
        assertThat(result).isInstanceOf(List.class); // Ensure the result is a List
        List<Product> products = (List<Product>) result; // Cast the result to a List of Products

        assertThat(products).isNotNull(); // Ensure the list of products is not null
        assertThat(products).hasSize(2); // Assuming two products were created

        // Additional assertions for each product
        assertThat(products.get(0).getName()).isEqualTo(product1); // Check the name of the first product
        assertThat(products.get(1).getName()).isEqualTo(product2); // Check the name of the second product
        // Add more assertions based on your product entity properties

        // You can also print the actual products for debugging
        System.out.println("Actual products: " + products);
    }

    @Test
    void testFindAllByNameOrDescription() {
        // Arrange
        // Create three test products with different names and descriptions
        Product product1 = new Product("Product1", "Description1", 100.0);
        Product product2 = new Product("Product2", "Description2", 150.0);
        Product product3 = new Product("AnotherProduct", "AnotherDescription", 200.0);

        // Save all three products to the repository
        productRepository.saveAll(List.of(product1, product2, product3));

        // Act
        // Call the findAllByNameOrDescription method with the keyword "Product"
        List<Product> result = productRepository.findAllByNameOrDescription("Product");

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(2); // Expecting two products with names or descriptions containing "Product"
        assertThat(result).contains(product1, product2); // Expecting product1 and product2 in the result
    }

    @Test
    void testFindAllByCategory() {
        // Arrange
        // Create a test category "Fish" and three products associated with it
        Category category = new Category("Fish");
        Product product1 = new Product("Salmon", "Fresh salmon fillet", 25.0, category, true, false);
        Product product2 = new Product("Shrimp", "Large tiger shrimp", 18.0, category, true, false);
        Product product3 = new Product("Calamari", "Sliced calamari rings", 15.0, category, true, false);

        // Save all three products to the repository
        productRepository.saveAll(List.of(product1, product2, product3));

        // Act
        // Call the findAllByCategory method with an empty category name (assuming an empty string is a valid case)
        List<Product> result = productRepository.findAllByCategory("");

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(3); // Expecting all three products in the result
        assertThat(result).contains(product1, product2, product3); // Expecting product1, product2, and product3 in the result
    }


    @Test
    void testRandomProduct() {
        // Arrange
        // Create a test category "Fish" and five products associated with it
        Category category = new Category("Fish");
        Product product1 = new Product("Salmon", "Fresh salmon fillet", 25.0, category, true, false);
        Product product2 = new Product("Shrimp", "Large tiger shrimp", 18.0, category, true, false);
        Product product3 = new Product("Calamari", "Sliced calamari rings", 15.0, category, true, false);
        Product product4 = new Product("Tuna", "Yellowfin tuna steak", 30.0, category, true, false);
        Product product5 = new Product("Lobster", "Whole lobster", 50.0, category, true, false);

        // Save all five products to the repository
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // Act
        // Call the randomProduct method to get a list of 9 random products
        List<Product> result = productRepository.randomProduct();

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(9); // Expecting a list of 9 products
        assertThat(result).containsExactlyInAnyOrder(product1, product2, product3, product4, product5); // Verify that all original products are present in the result
    }


    @Test
    void testFilterLowerProducts() {
        // Arrange
        // Create a test category "Fish" and five products associated with it
        Category category = new Category("Fish");
        Product product1 = new Product("Salmon", "Fresh salmon fillet", 25.0, category, true, false);
        Product product2 = new Product("Shrimp", "Large tiger shrimp", 18.0, category, true, false);
        Product product3 = new Product("Calamari", "Sliced calamari rings", 15.0, category, true, false);
        Product product4 = new Product("Tuna", "Yellowfin tuna steak", 30.0, category, true, false);
        Product product5 = new Product("Lobster", "Whole lobster", 50.0, category, true, false);

        // Save all five products to the repository
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // Act
        // Call the filterLowerProducts method to get a list of 3 products sorted by cost price in ascending order
        List<Product> result = productRepository.filterLowerProducts();

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(3); // Expecting a list of 3 products
        assertThat(result).isSortedAccordingTo((p1, p2) -> Double.compare(p1.getCostPrice(), p2.getCostPrice())); // Verify that the result is sorted by cost price in ascending order
    }

    @Test
    void testListViewProduct() {
        // Arrange
        // Create a test category "Fish" and five products associated with it
        Category category = new Category("Fish");
        Product product1 = new Product("Salmon", "Fresh salmon fillet", 25.0, category, true, false);
        Product product2 = new Product("Shrimp", "Large tiger shrimp", 18.0, category, true, false);
        Product product3 = new Product("Calamari", "Sliced calamari rings", 15.0, category, true, false);
        Product product4 = new Product("Tuna", "Yellowfin tuna steak", 30.0, category, true, false);
        Product product5 = new Product("Lobster", "Whole lobster", 50.0, category, true, false);

        // Save all five products to the repository
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // Act
        // Call the listViewProduct method to get a list of 4 products
        List<Product> result = productRepository.listViewProduct();

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(4); // Expecting a list of 4 products
    }

    @Test
    void testGetProductByCategoryId() {
        // Arrange
        // Create a test category "Fish" and five products associated with it
        Category category = new Category("Fish");
        Product product1 = new Product("Salmon", "Fresh salmon fillet", 25.0, category, true, false);
        Product product2 = new Product("Shrimp", "Large tiger shrimp", 18.0, category, true, false);
        Product product3 = new Product("Calamari", "Sliced calamari rings", 15.0, category, true, false);
        Product product4 = new Product("Tuna", "Yellowfin tuna steak", 30.0, category, true, false);
        Product product5 = new Product("Lobster", "Whole lobster", 50.0, category, true, false);

        // Save all five products to the repository
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // Act
        // Call the getProductByCategoryId method to get a list of 5 products with the given category id
        List<Product> result = productRepository.getProductByCategoryId(category.getId());

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(5); // Expecting a list of 5 products with the given category id, is_deleted = false, and is_activated = true
    }


    @Test
    void testSearchProducts() {
        // Arrange
        // Create a test category "Fish" and five products associated with it
        Category category = new Category("Fish");
        Product product1 = new Product("Salmon", "Fresh salmon fillet", 25.0, category, true, false);
        Product product2 = new Product("Shrimp", "Large tiger shrimp", 18.0, category, true, false);
        Product product3 = new Product("Calamari", "Sliced calamari rings", 15.0, category, true, false);
        Product product4 = new Product("Tuna", "Yellowfin tuna steak", 30.0, category, true, false);
        Product product5 = new Product("Lobster", "Whole lobster", 50.0, category, true, false);

        // Save all five products to the repository
        productRepository.saveAll(List.of(product1, product2, product3, product4, product5));

        // Act
        // Call the searchProducts method with the keyword  (assuming it's not in the product names or descriptions)
        List<Product> result = productRepository.searchProducts("Laptop");

        // Assert
        assertThat(result).isNotNull(); // Ensure the result is not null
        assertThat(result).hasSize(1); // Expecting one product with the keyword
        assertThat(result.get(0).getName()).isEqualTo("Salmon"); // Verify that the product found is "Salmon"
    }
}





