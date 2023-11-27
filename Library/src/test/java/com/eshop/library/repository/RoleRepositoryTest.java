package com.eshop.library.repository;

import com.eshop.library.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findByName() {
        // Arrange
        // Create a new Role instance with the name "ROLE_CUSTOMER"
        Role role = new Role();
        role.setName("ROLE_CUSTOMER");

        // Persist and flush the role entity to the test database
        // This ensures the role is saved and available for retrieval
        testEntityManager.persistAndFlush(role);

        // Act
        // Call the findByName method on the roleRepository to retrieve the role by name
        Role foundRole = roleRepository.findByName("ROLE_CUSTOMER");

        // Assert
        // Ensure that the foundRole is not null, indicating a successful retrieval
        assertThat(foundRole).isNotNull();
        // Additional assertions can be added here based on the specific requirements
    }
    @Test
    void findByName_NotFound() {
        // Act
        // Retrieving a non-existent role by name from the repository
        Role foundRole = roleRepository.findByName("NON_EXISTENT_ROLE");

        // Assert
        // Verifying that the found role is null as it should not exist
        assertThat(foundRole).isNull();
    }
}

