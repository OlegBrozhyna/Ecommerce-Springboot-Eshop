package com.eshop.library.service.impl;

import com.eshop.library.model.Category;
import com.eshop.library.repository.CategoryRepository;
import com.eshop.library.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    // Method to save a new category
    @Override
    public Category save(Category category) {
        // Create a new category instance with the provided name and save it to the repository
        Category categorySave = new Category(category.getName());
        return categoryRepository.save(categorySave);
    }

    // Method to update an existing category
    @Override
    public Category update(Category category) {
        // Retrieve the category by its ID, update its name, and save the changes
        Category categoryUpdate = categoryRepository.getById(category.getId());
        categoryUpdate.setName(category.getName());
        return categoryRepository.save(categoryUpdate);
    }

    // Method to retrieve all activated categories
    @Override
    public List<Category> findAllByActivatedTrue() {
        // Retrieve and return a list of all activated categories
        return categoryRepository.findAllByActivatedTrue();
    }

    // Method to retrieve all categories
    @Override
    public List<Category> findALl() {
        // Retrieve and return a list of all categories
        return categoryRepository.findAll();
    }

    // Method to find a category by its ID
    @Override
    public Optional<Category> findById(Long id) {
        // Retrieve a category by its ID and return it as an Optional
        return categoryRepository.findById(id);
    }

    // Method to logically delete a category by setting it as deactivated and deleted
    @Override
    public void deleteById(Long id) {
        // Retrieve the category by its ID, deactivate it, and mark it as deleted
        Category category = categoryRepository.getById(id);
        category.setActivated(false);
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    // Method to enable a category by setting it as activated and not deleted
    @Override
    public void enableById(Long id) {
        // Retrieve the category by its ID, activate it, and mark it as not deleted
        Category category = categoryRepository.getById(id);
        category.setActivated(true);
        category.setDeleted(false);
        categoryRepository.save(category);
    }


    // Method to retrieve a list of categories along with their sizes
//    public List<CategoryDto> getCategoriesAndSize() {
//        List<CategoryDto> categories = categoryRepository.getCategoriesAndSize();
//        return categories;
//    }
}
