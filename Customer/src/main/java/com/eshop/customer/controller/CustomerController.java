package com.eshop.customer.controller;

import com.eshop.library.dto.CustomerDto;
import com.eshop.library.model.City;
import com.eshop.library.model.Country;
import com.eshop.library.service.CityService;
import com.eshop.library.service.CountryService;
import com.eshop.library.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;  // Injected CustomerService for managing customer data
    private final CountryService countryService;    // Injected CountryService for managing country data
    private final PasswordEncoder passwordEncoder;  // Injected PasswordEncoder for encoding and validating passwords
    private final CityService cityService;          // Injected CityService for managing city data

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        // Check if the user is authenticated (logged in), and show their profile
        if (principal == null) {
            return "redirect:/login";  // Redirect to the login page if not authenticated
        } else {
            String username = principal.getName();
            // Retrieve the customer's data and necessary lists
            CustomerDto customer = customerService.getCustomer(username);
            List<Country> countryList = countryService.findAll();
            List<City> cities = cityService.findAll();
            // Add the data to the model for rendering the profile page
            model.addAttribute("customer", customer);
            model.addAttribute("cities", cities);
            model.addAttribute("countries", countryList);
            model.addAttribute("title", "Profile");
            model.addAttribute("page", "Profile");
            return "customer-information";  // Display the customer's profile information
        }
    }

    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
                                BindingResult result,
                                RedirectAttributes attributes,
                                Model model,
                                Principal principal) {
        // Check if the user is authenticated (logged in) and update their profile
        if (principal == null) {
            return "redirect:/login";  // Redirect to the login page if not authenticated
        } else {
            String username = principal.getName();
            // Retrieve the customer's data and necessary lists
            CustomerDto customer = customerService.getCustomer(username);
            List<Country> countryList = countryService.findAll();
            List<City> cities = cityService.findAll();
            model.addAttribute("countries", countryList);
            model.addAttribute("cities", cities);
            if (result.hasErrors()) {
                return "customer-information";  // Return to the profile page with validation errors if any
            }
            customerService.update(customerDto);  // Update the customer's profile
            CustomerDto customerUpdate = customerService.getCustomer(principal.getName());
            attributes.addFlashAttribute("success", "Update successfully!");  // Flash message for success
            model.addAttribute("customer", customerUpdate);
            return "redirect:/profile";  // Redirect back to the profile page after the update
        }
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        // Check if the user is authenticated (logged in) and show the change password page
        if (principal == null) {
            return "redirect:/login";  // Redirect to the login page if not authenticated
        }
        model.addAttribute("title", "Change password");
        model.addAttribute("page", "Change password");
        return "change-password";  // Display the change password page
    }

    @PostMapping("/change-password")
    public String changePass(@RequestParam("oldPassword") String oldPassword,
                             @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatNewPassword") String repeatPassword,
                             RedirectAttributes attributes,
                             Model model,
                             Principal principal) {
        // Check if the user is authenticated (logged in) and change their password
        if (principal == null) {
            return "redirect:/login";  // Redirect to the login page if not authenticated
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());
            if (passwordEncoder.matches(oldPassword, customer.getPassword())
                    && !passwordEncoder.matches(newPassword, oldPassword)
                    && !passwordEncoder.matches(newPassword, customer.getPassword())
                    && repeatPassword.equals(newPassword) && newPassword.length() >= 5) {
                customer.setPassword(passwordEncoder.encode(newPassword));
                customerService.changePass(customer);  // Update the customer's password
                attributes.addFlashAttribute("success", "Your password has been changed successfully!");  // Flash message for success
                return "redirect:/profile";  // Redirect back to the profile page after password change
            } else {
                model.addAttribute("message", "Your password is wrong");  // Display an error message if conditions are not met
                return "change-password";  // Return to the change password page
            }
        }
    }
}
