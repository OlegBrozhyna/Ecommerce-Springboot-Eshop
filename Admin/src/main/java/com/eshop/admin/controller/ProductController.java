package com.eshop.admin.controller;

import com.eshop.library.dto.ProductDto;
import com.eshop.library.model.Category;
import com.eshop.library.service.CategoryService;
import com.eshop.library.service.impl.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    // Get all products
    @GetMapping("/products")
    public String products(Model model) {
        // Retrieve a list of all products and add them to the model
        List<ProductDto> products = productService.allProduct();
        model.addAttribute("products", products);
        model.addAttribute("size", products.size());

        // Check if the user is authenticated and redirect to the login page if necessary
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "products";
    }

    // Get products by a specific page
    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        // Check if the user is authenticated and redirect to the login page if necessary
        if (principal == null) {
            return "redirect:/login";
        }
        // Retrieve a list of products to display on a specific page
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "products";
    }

    // Search products by keyword and page
    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
                                @RequestParam(value = "keyword") String keyword,
                                Model model
    ) {
        // Search for products by keyword and display them on a specific page
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return "product-result";
    }

    // Create a page for adding a new product
    @GetMapping("/add-product")
    public String addProductPage(Model model) {
        model.addAttribute("title", "Add Product");
        // Retrieve a list of activated categories for selection
        List<Category> categories = categoryService.findAllByActivatedTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", new ProductDto());
        // Check if the user is authenticated and redirect to the login page if necessary
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        return "add-product";
    }

    // Save a new product
    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.save(imageProduct, product);
            redirectAttributes.addFlashAttribute("success", "Add new product successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add a new product!");
        }
        return "redirect:/products/0";
    }

    // Create a page for updating a product
    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }
        // Retrieve a list of activated categories for selection and the details of the product to update
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("title", "Update Product");
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        return "update-product";
    }

    // Update a product
    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.update(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Server error, please try again!");
        }
        return "redirect:/products/0";
    }

    // Enable a product
    @RequestMapping(value = "/enable-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/products/0";
    }

    // Delete a product
    @RequestMapping(value = "/delete-product", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/products/0";
    }
}
