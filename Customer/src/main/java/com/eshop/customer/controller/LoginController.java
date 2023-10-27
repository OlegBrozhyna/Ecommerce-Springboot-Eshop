package com.eshop.customer.controller;

import com.eshop.library.dto.CustomerDto;
import com.eshop.library.model.Customer;
import com.eshop.library.service.CustomerService;
import com.nimbusds.oauth2.sdk.Request;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class LoginController {
    private final CustomerService customerService; // Dependency injection for CustomerService
    private final BCryptPasswordEncoder passwordEncoder; // Dependency injection for BCryptPasswordEncoder
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("title", "Login Page"); // Set the title attribute in the model
        model.addAttribute("page", "Home"); // Set the page attribute in the model
        return "login"; // Return the view name "login"
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register"); // Set the title attribute in the model
        model.addAttribute("page", "Register"); // Set the page attribute in the model
        model.addAttribute("customerDto", new CustomerDto()); // Create and add a new CustomerDto to the model
        return "register"; // Return the view name "register"
    }

    @PostMapping("/do-register")
    public String registerCustomer(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                   BindingResult result,
                                   Model model) {
        try {
            if (result.hasErrors()) { // Check if there are validation errors
                model.addAttribute("customerDto", customerDto); // Add the CustomerDto to the model
                return "register"; // Return the "register" view to show validation errors
            }
            String username = customerDto.getUsername();
            Customer customer = customerService.findByUsername(username); // Find a customer by username
            if (customer != null) {
                model.addAttribute("customerDto", customerDto);
                model.addAttribute("error", "Email has been registered!"); // Add an error message to the model
                return "register"; // Return the "register" view to show the error message
            }
            if (customerDto.getPassword().equals(customerDto.getConfirmPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword())); // Encode the password
                customerService.save(customerDto); // Save the customer data
                model.addAttribute("success", "Registered successfully!"); // Add a success message to the model
            } else {
                model.addAttribute("error", "Password is incorrect"); // Add an error message for password mismatch
                model.addAttribute("customerDto", customerDto);
                return "register"; // Return the "register" view to show the error message
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Server error, please try again later!"); // Handle server error
        }
        return "register"; // Return the "register" view
    }
}
