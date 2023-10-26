package com.eshop.customer.controller;


import com.eshop.library.dto.CategoryDto;
import com.eshop.library.dto.ProductDto;
import com.eshop.library.model.Category;
import com.eshop.library.service.CategoryService;
import com.eshop.library.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    // Handles the "/menu" GET request to display a menu of products.
    @GetMapping("/menu")
    public String menu(Model model) {
        // Set attributes for the model to display on the menu page.
        model.addAttribute("page", "Products");
        model.addAttribute("title", "Menu");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        List<ProductDto> products = productService.products();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "index";
    }

    // Handles the "/product-detail/{id}" GET request to display details of a specific product.
    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        // Retrieve product details and related products by category.
        ProductDto product = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(product.getCategory().getName());
        // Set attributes for the model to display on the product detail page.
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail", product);
        return "product-detail";
    }

    // Handles the "/shop-detail" GET request to display shop details.
    @GetMapping("/shop-detail")
    public String shopDetail(Model model) {
        // Retrieve categories with sizes and random products for display.
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        List<ProductDto> products = productService.randomProduct();
        List<ProductDto> listView = productService.listViewProducts();
        // Set attributes for the model to display on the shop detail page.
        model.addAttribute("categories", categories);
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    // Handles the "/high-price" GET request to filter products by high price.
    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        // Retrieve categories with sizes and high-priced products for display.
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        List<ProductDto> products = productService.filterHighProducts();
        List<ProductDto> listView = productService.listViewProducts();
        // Set attributes for the model to display on the shop detail page.
        model.addAttribute("categories", categories);
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    // Handles the "/lower-price" GET request to filter products by low price.
    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model) {
        // Retrieve categories with sizes and low-priced products for display.
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        List<ProductDto> products = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        // Set attributes for the model to display on the shop detail page.
        model.addAttribute("categories", categories);
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    // Handles the "/find-products/{id}" GET request to display products in a specific category.
    @GetMapping("/find-products/{id}")
    public String productsInCategory(@PathVariable("id") Long id, Model model) {
        // Retrieve categories with sizes and products in a specific category for display.
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<ProductDto> listView = productService.listViewProducts();
        // Set attributes for the model to display on the products page.
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", productDtos);
        model.addAttribute("productViews", listView);
        return "products";
    }

    // Handles the "/search-product" GET request to search for products based on a keyword.
    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        // Retrieve categories with sizes and products matching the search keyword.
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.searchProducts(keyword);
        List<ProductDto> listView = productService.listViewProducts();
        // Set attributes for the model to display on the products page.
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", "Search Products");
        model.addAttribute("page", "Result Search");
        model.addAttribute("products", productDtos);
        model.addAttribute("productViews", listView);
        return "products";
    }
}
