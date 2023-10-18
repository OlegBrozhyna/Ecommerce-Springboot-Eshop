package com.eshop.library.repository;

import com.eshop.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
        // Retrieve a list of all products that are not deleted and are activated.
        @Query("select p from Product p where p.is_deleted = false and p.is_activated = true")
        List<Product> getAllProduct();

        // Search for products by a keyword in their name or description.
        @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
        List<Product> findAllByNameOrDescription(String keyword);

        // Retrieve products within a specific category that are activated and not deleted.
        @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
                " where p.category.name = ?1 and p.is_activated = true and p.is_deleted = false")
        List<Product> findAllByCategory(String category);

        // Retrieve a list of random products that are activated and not deleted.
        @Query(value = "select " +
                "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
                "from products p where p.is_activated = true and p.is_deleted = false order by rand() limit 9", nativeQuery = true)
        List<Product> randomProduct();

        // Retrieve a list of products sorted by high cost/price, which are activated and not deleted.
        @Query(value = "select " +
                "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
                "from products p where p.is_deleted = false and p.is_activated = true order by p.cost_price desc limit 9", nativeQuery = true)
        List<Product> filterHighProducts();

        // Retrieve a list of products sorted by low cost/price, which are activated and not deleted.
        @Query(value = "select " +
                "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
                "from products p where p.is_deleted = false and p.is_activated = true order by p.cost_price asc limit 9", nativeQuery = true)
        List<Product> filterLowerProducts();

        // Retrieve a list of products in a specific view format, activated and not deleted.
        @Query(value = "select p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.category_id, p.sale_price, p.image, p.is_activated, p.is_deleted from products p where p.is_deleted = false and p.is_activated = true limit 4", nativeQuery = true)
        List<Product> listViewProduct();

        // Retrieve products by a specific category ID that are activated and not deleted.
        @Query(value = "select p from Product p inner join Category c on c.id = ?1 and p.category.id = ?1 where p.is_activated = true and p.is_deleted = false")
        List<Product> getProductByCategoryId(Long id);

        // Search for products by a keyword in their name or description.
        @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
        List<Product> searchProducts(String keyword);
}

