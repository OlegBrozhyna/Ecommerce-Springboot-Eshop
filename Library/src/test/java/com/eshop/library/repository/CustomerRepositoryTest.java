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
    private EntityManager entityManager; // Add missing semicolon here

    @Test
    public void testFindByUsername() {
        // Arrange
        Customer customer = new Customer("user12@gamail.com", "John Doe", "12345");
        customerRepository.save(customer);

        // Force synchronization with the database
        entityManager.flush();

        // Act
        Customer foundCustomer = customerRepository.findByUsername("user12@gamail.com");

        // Assert
        assertThat(foundCustomer)
                .isNotNull()
                .extracting(Customer::getUsername, Customer::getLastName, Customer::getPassword)
                .containsExactly("user12@gamail.com", "John Doe", "12345")
                .doesNotContainNull(); // Make sure none of the values are null

        assertThat(foundCustomer.getUsername()).isEqualToIgnoringCase("user12@gamail.com");
    }

    @Test
    public void testFindByUsername_NotFound() {
        // Act
        Customer foundCustomer = customerRepository.findByUsername("nonexistent_user");

        // Assert
        assertThat(foundCustomer).isNull();
    }
}

