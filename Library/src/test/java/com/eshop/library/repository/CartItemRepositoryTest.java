package com.eshop.library.repository;

import com.eshop.library.model.CartItem;
import com.eshop.library.model.ShoppingCart;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        CartItem cartItem = new CartItem();
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        // Act
        cartItemRepository.deleteCartItemById(savedCartItem.getId());
        entityManager.flush();

        // Assert
        assertFalse(cartItemRepository.existsById(savedCartItem.getId()));
    }
}

//    @Test
//    void deleteCartItemById() {
//        // Arrange
//        CartItem cartItem = new CartItem();
//        CartItem savedCartItem = cartItemRepository.save(cartItem);
//
//        // Act
//        cartItemRepository.deleteCartItemById(savedCartItem.getId());
//        entityManager.flush();
//
//        // Assert
//        assertTrue(cartItemRepository.existsById(savedCartItem.getId()));
//
//    }
//}