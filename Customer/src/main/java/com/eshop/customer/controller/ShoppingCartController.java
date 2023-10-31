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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
    @RequiredArgsConstructor
    public class ShoppingCartController {
    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            if (cart == null) {
                model.addAttribute("check", true); // Позначає, що кошик порожній
            } else {
                model.addAttribute("grandTotal", cart.getTotalPrice());
            }
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");
            return "cart";
        }
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) {


        ProductDto productDto = productService.getById(id);
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.addItemToCart(productDto, quantity, username);
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            model.addAttribute("shoppingCart", shoppingCart);
        }
        return "redirect:" + request.getHeader("Referer");
    }
}