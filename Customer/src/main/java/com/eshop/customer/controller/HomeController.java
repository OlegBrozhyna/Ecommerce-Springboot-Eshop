package com.eshop.customer.controller;
import com.eshop.library.model.Customer;
import com.eshop.library.model.ShoppingCart;
import com.eshop.library.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CustomerService customerService;

    // This method handles requests to the home page ("/" or "/index").
    // It sets the "title" and "page" attributes in the model.
    // If a user is authenticated (principal not null), it also sets the "username" and "totalItems" in the session.
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");

        if (principal != null) {
            // Retrieve customer information based on the authenticated user's username.
            Customer customer = customerService.findByUsername(principal.getName());

            // Set the "username" attribute in the session to the user's full name.
            session.setAttribute("username", customer.getFirstName() + " " + customer.getLastName());

            // If the customer has a shopping cart, set the "totalItems" attribute in the session.
            ShoppingCart shoppingCart = customer.getCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }
        }

        // Return the view name "home" to render the home page.
        return "home";
    }

    // This method handles requests to the "contact" page and sets the "title" and "page" attributes in the model.
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact");
        model.addAttribute("page", "Contact");

        // Return the view name "contact-us" to render the contact page.
        return "contact-us";
    }
}
