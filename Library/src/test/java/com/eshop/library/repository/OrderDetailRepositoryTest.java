package com.eshop.library.repository;

import com.eshop.library.model.Customer;
import com.eshop.library.model.Order;
import com.eshop.library.model.OrderDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository; // Autowired OrderDetailRepository for testing

    @Autowired
    private TestEntityManager testEntityManager; // Autowired TestEntityManager for managing test data

    @Test
    void findAllByCustomerId() {
        // Arrange
        // Creating a new Customer entity and persisting it to the database
        Customer customer = new Customer("user12@gamail.com", "John Doe", "12345");
        testEntityManager.persistAndFlush(customer);

        // Creating two OrderDetail entities associated with the customer
        OrderDetail order1 = new OrderDetail(customer);
        OrderDetail order2 = new OrderDetail(customer);

        // Persisting and flushing the OrderDetail entities to the database
        testEntityManager.persistAndFlush(order1);
        testEntityManager.persistAndFlush(order2);

        // Act
        // Retrieving orders associated with the customer from the repository
        List<Order> orders = orderDetailRepository.findAllByCustomerId(customer.getId());

        // Assert
        // Verifying that the list of orders is not null and has the expected size
        assertThat(orders).isNotNull();
        assertThat(orders).hasSize(2); // Assuming two orders were created for the customer
    }
}

