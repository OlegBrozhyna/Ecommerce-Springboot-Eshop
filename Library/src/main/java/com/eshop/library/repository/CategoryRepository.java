package com.eshop.library.repository;

import com.eshop.library.dto.CategoryDto;
import com.eshop.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Custom method to update the name of a category by its ID.
    @Modifying
    @Query(value = "update Category set name = ?1 where id = ?2")
    int update(@Param("name") String name, @Param("id") Long id);

    // Custom method to find all categories that are activated (using a native SQL query).
    @Query(value = "select * from categories where is_activated = true", nativeQuery = true)
    List<Category> findAllByActivatedTrue();

    // Custom method to retrieve a list of CategoryDto objects containing category details and the count of products in each category.
    @Query(value = "select new com.eshop.library.dto.CategoryDto(c.id, c.name, count(p.category.id)) " +
            "from Category c left join Product p on c.id = p.category.id " +
            "where c.activated = true and c.deleted = false " +
            "group by c.id ")
    List<CategoryDto> getCategoriesAndSize();
}
