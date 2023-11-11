package com.eshop.customer.controller;

import com.eshop.library.dto.ProductDto;
import com.eshop.library.model.Customer;
import com.eshop.library.model.ShoppingCart;
import com.eshop.library.service.CustomerService;
import com.eshop.library.service.ProductService;
import com.eshop.library.service.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    // Handles GET requests for the "/cart" endpoint to display the shopping cart.
    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        // Check if the user is not authenticated, redirect to the login page.
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve customer information based on the authenticated username.
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();

            // Add attributes to the model for rendering in the view.
            if (cart == null) {
                model.addAttribute("check", true); // Indicates an empty cart.
            } else {
                model.addAttribute("grandTotal", cart.getTotalPrice());
            }
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");

            // Return the view name for rendering the shopping cart details.
            return "cart";
        }
    }

    // Handles POST requests to add items to the shopping cart.
    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) {

        // Check if the user is not authenticated, redirect to the login page.
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve the authenticated username.
            String username = principal.getName();

            // Add the selected item to the shopping cart.
            ShoppingCart shoppingCart = cartService.addItemToCart(productService.getById(id), quantity, username);

            // Update the total items attribute in the session.
            session.setAttribute("totalItems", shoppingCart.getTotalItems());

            // Add the shopping cart attribute to the model for rendering.
            model.addAttribute("shoppingCart", shoppingCart);
        }

        // Redirect back to the previous page.
        return "redirect:" + request.getHeader("Referer");
    }

    // Handles POST requests to update the quantity of items in the shopping cart.
    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") int quantity,
                             Model model,
                             Principal principal) {
        // Check if the user is not authenticated, redirect to the login page.
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve product information based on the provided ID.
            ProductDto productDto = productService.getById(id);

            // Retrieve the authenticated username.
            String username = principal.getName();

            // Update the shopping cart with the modified item quantity.
            ShoppingCart shoppingCart = cartService.updateCart(productDto, quantity, username);

            // Add the shopping cart attribute to the model for rendering.
            model.addAttribute("shoppingCart", shoppingCart);

            // Redirect to the shopping cart page.
            return "redirect:/cart";
        }
    }

    // Handles POST requests to delete items from the shopping cart.
    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    public String deleteItem(@RequestParam("id") Long id,
                             Model model,
                             Principal principal) {
        // Check if the user is not authenticated, redirect to the login page.
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve product information based on the provided ID.
            ProductDto productDto = productService.getById(id);

            // Retrieve the authenticated username.
            String username = principal.getName();

            // Remove the specified item from the shopping cart.
            ShoppingCart shoppingCart = cartService.removeItemFromCart(productDto, username);

            // Add the shopping cart attribute to the model for rendering.
            model.addAttribute("shoppingCart", shoppingCart);

            // Redirect to the shopping cart page.
            return "redirect:/cart";
        }
    }
}
