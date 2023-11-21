package com.eshop.library.repository;

import com.eshop.library.model.CartItem;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void deleteCartItemById() {
        // Arrange
        // Create a new CartItem instance.
        CartItem cartItem = new CartItem();
        // Save the CartItem to the repository and get the saved instance.
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Act
        // Delete the CartItem by calling the custom delete method.
        cartItemRepository.deleteCartItemById(savedCartItem.getId());
        // Flush the changes to the database.

        // Assert
        // Ensure that the CartItem no longer exists in the repository.
        assertFalse(cartItemRepository.existsById(savedCartItem.getId()));
    }
}
