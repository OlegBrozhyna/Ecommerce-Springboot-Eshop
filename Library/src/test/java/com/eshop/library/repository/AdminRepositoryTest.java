package com.eshop.library.repository;

import com.eshop.library.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void findByUsername_WhenAdminExists_thenReturnAdmin() {
        // Arrange
        // Create an Admin object with valid data
        Admin admin = new Admin();
        admin.setUsername("admin@gmail.com");
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setPassword("12345");

        // Save admin to the database using the repository
        adminRepository.save(admin);
        // Save to the database and capture the object ID for later retrieval

        entityManager.flush(); // Execute the database write to make data available

        // Act
        // Call the findByUsername method to search for an admin with the specified username
        Admin foundAdmin = adminRepository.findByUsername("admin@gmail.com");

        // Assert
        // Ensure that the found admin is not null
        assertThat(foundAdmin).isNotNull();
        // Check that the admin's username matches the expected value
        assertThat(foundAdmin.getUsername()).isEqualTo("admin@gmail.com");
        // Add additional checks based on Admin entity properties

        // However, this test needs more checks to ensure correctness
        // Consider checking all fields of the admin for a comprehensive test
    }

    @Test
    void findByUsername_WhenAdminDoesNotExist_thenReturnNull() {
        // Act
        // Call the findByUsername method to search for an admin with a username that definitely doesn't exist
        Admin foundAdmin = adminRepository.findByUsername("admin");

        // Assert
        // Ensure that the search result is null since there is no admin with such username
        assertThat(foundAdmin).isNull();
    }
}
