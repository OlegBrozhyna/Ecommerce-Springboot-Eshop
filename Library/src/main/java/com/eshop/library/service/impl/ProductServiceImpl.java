package com.eshop.library.service.impl;

import com.eshop.library.dto.ProductDto;
import com.eshop.library.model.Product;
import com.eshop.library.repository.ProductRepository;
import com.eshop.library.utils.ImageUpload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ImageUpload imageUpload;

    @Override
    public List<Product> findAll() {
        // Retrieve and return a list of all products from the repository.
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> products() {
        // Retrieve and return a list of product data transfer objects (DTOs) for all products.
        return transferData(productRepository.getAllProduct());
    }

    @Override
    public List<ProductDto> allProduct() {
        // Retrieve all products from the repository and convert them into DTOs.
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    @Override
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        // Create a new product, populate its attributes, and save it to the repository.
        Product product = new Product();
        try {
            // Handle product image upload and encoding if an image is provided.
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            // Set other product attributes from the provided ProductDto.
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCostPrice(productDto.getCostPrice());
            product.setCategory(productDto.getCategory());
            product.set_deleted(false);
            product.set_activated(true);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        // Update an existing product with the provided data.
        try {
            // Retrieve the existing product and update its attributes.
            Product productUpdate = productRepository.getById(productDto.getId());

            // Handle image update and encoding if a new image is provided.
            if (imageProduct == null) {
                productUpdate.setImage(productUpdate.getImage());
            } else {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
            // Set other product attributes from the provided ProductDto.
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setId(productUpdate.getId());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setCostPrice(productDto.getCostPrice());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setCurrentQuantity(productDto.getCurrentQuantity());
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void enableById(Long id) {
        // Enable a product by setting it as activated and not deleted.
        Product product = productRepository.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        // Delete a product by setting it as deleted and not activated.
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        // Retrieve a specific product by ID and convert it to a ProductDto.
        ProductDto productDto = new ProductDto();
        Product product = productRepository.getById(id);

        // Populate the ProductDto with product information.
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());

        return productDto;
    }
    @Override
    public Product findById(Long id) {
        // Retrieve and return a specific product by its ID.
        return productRepository.findById(id).get();
    }

    @Override
    public List<ProductDto> randomProduct() {
        // Retrieve and return a list of random products as DTOs.
        return transferData(productRepository.randomProduct());
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        // Search for products by a keyword in their name or description and return as a paginated result.
        List<Product> products = productRepository.findAllByNameOrDescription(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    @Override
    public Page<ProductDto> getAllProducts(int pageNo) {
        // Retrieve all products as DTOs and return them in a paginated form.
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<ProductDto> productDtoLists = this.allProduct();
        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
        return productDtoPage;
    }

    @Override
    public Page<ProductDto> getAllProductsForCustomer(int pageNo) {
        // This method may be implemented to retrieve products for customer view.
        // As of now, it returns null and may require further implementation.
        return null;
    }

    @Override
    public List<ProductDto> findAllByCategory(String category) {
        // Retrieve and return a list of products within the specified category as DTOs.
        return transferData(productRepository.findAllByCategory(category));
    }

    @Override
    public List<ProductDto> filterHighProducts() {
        // Retrieve and return a list of products with high values (e.g., prices) as DTOs.
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        // Retrieve and return a list of products with low values (e.g., prices) as DTOs.
        return transferData(productRepository.filterLowerProducts());
    }

    @Override
    public List<ProductDto> listViewProducts() {
        // Retrieve and return a list of products in a specific view format (e.g., list view) as DTOs.
        return transferData(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        // Retrieve and return a list of products by their category ID as DTOs.
        return transferData(productRepository.getProductByCategoryId(id));
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        // Search for products by a keyword and return them as DTOs.
        return transferData(productRepository.searchProducts(keyword));
    }

    private Page toPage(List list, Pageable pageable) {
        // Check if the requested page offset is beyond the size of the input list.
        if (pageable.getOffset() >= list.size()) {
            // If the offset is beyond the list size, return an empty Page.
            return Page.empty();
        }
        // Calculate the start index for the subset of the list based on the page offset.
        int startIndex = (int) pageable.getOffset();

        // Calculate the end index for the subset of the list based on the page offset
        // and page size, ensuring it doesn't exceed the size of the input list.
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());

        // Create a sublist of the input list based on the calculated start and end indexes.
        List subList = list.subList(startIndex, endIndex);

        // Create and return a PageImpl object that represents the paginated subset of the input list.
        // The PageImpl constructor takes the sublist, the original Pageable, and the total size of the list.
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ProductDto> transferData(List<Product> products) {
        // Convert a list of Product objects to a list of ProductDto objects.
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            // Create ProductDto instances and populate them with product information.
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.is_activated());
            productDto.setDeleted(product.is_deleted());
            productDtos.add(productDto);
        }
        return productDtos;
    }
}



