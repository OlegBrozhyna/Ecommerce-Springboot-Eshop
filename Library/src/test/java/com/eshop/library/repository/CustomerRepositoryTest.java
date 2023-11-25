package com.eshop.library.repository;

import com.eshop.library.model.Customer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager; // Entity manager for interacting with the database

    @Test
    public void testFindByUsername() {
        // Arrange
        // Creating a new Customer entity
        Customer customer = new Customer("user12@gamail.com", "John Doe", "12345");

        // Saving the customer to the database
        customerRepository.save(customer);

        // Force synchronization with the database
        entityManager.flush();

        // Act
        // Retrieving the customer by username
        Customer foundCustomer = customerRepository.findByUsername("user12@gamail.com");

        // Assert
        // Performing assertions on the retrieved customer
        assertThat(foundCustomer)
                .isNotNull() // Making sure the customer is not null
                .extracting(Customer::getUsername, Customer::getLastName, Customer::getPassword)
                .containsExactly("user12@gamail.com", "John Doe", "12345") // Checking individual fields
                .doesNotContainNull(); // Make sure none of the values are null

        // Additional assertion to check username case-insensitively
        assertThat(foundCustomer.getUsername()).isEqualToIgnoringCase("user12@gamail.com");
    }

    @Test
    public void testFindByUsername_NotFound() {
        // Act
        // Retrieving a non-existent customer by username
        Customer foundCustomer = customerRepository.findByUsername("nonexistent_user");

        // Assert
        // Verifying that the result is null for a non-existent user
        assertThat(foundCustomer).isNull();
    }
}

