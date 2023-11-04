package com.eshop.customer.controller;

import com.eshop.library.dto.CustomerDto;
import com.eshop.library.model.*;
import com.eshop.library.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ShoppingCartService cartService;
    private final CountryService countryService;
    private final CityService cityService;

    // Method to handle the checkout process
    @GetMapping("/check-out")
    public String checkOut(Principal principal, Model model) {
        // Redirect to login if not authenticated
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve customer information and check if details are complete
            CustomerDto customer = customerService.getCustomer(principal.getName());
            if (customer.getAddress() == null || customer.getCity() == null || customer.getPhoneNumber() == null) {
                // Inform the user to update their information before checkout and provide necessary data
                model.addAttribute("information", "You need to update your information before check out");
                List<Country> countryList = countryService.findAll();
                List<City> cities = cityService.findAll();
                model.addAttribute("customer", customer);
                model.addAttribute("cities", cities);
                model.addAttribute("countries", countryList);
                model.addAttribute("title", "Profile");
                model.addAttribute("page", "Profile");
                return "customer-information";
            } else {
                // If customer details are complete, proceed to checkout and display cart information
                ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
                model.addAttribute("customer", customer);
                model.addAttribute("title", "Check-Out");
                model.addAttribute("page", "Check-Out");
                model.addAttribute("shoppingCart", cart);
                model.addAttribute("grandTotal", cart.getTotalItems());
                return "checkout";
            }
        }
    }

    // Method to retrieve and display customer orders
    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        // Redirect to login if not authenticated
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve customer orders and display them
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Order");
            model.addAttribute("page", "Order");
            return "order";
        }
    }

    // Method to cancel an order
    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, RedirectAttributes attributes) {
        // Cancel the specified order and provide success message
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success", "Order canceled successfully!");
        return "redirect:/orders";
    }

    // Method to create a new order
    @RequestMapping(value = "/add-order", method = {RequestMethod.POST})
    public String createOrder(Principal principal, Model model, HttpSession session) {
        // Redirect to login if not authenticated
        if (principal == null) {
            return "redirect:/login";
        } else {
            // Retrieve the customer's cart, create a new order, and provide order details
            Customer customer = customerService.findByUsername(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Order Detail");
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Order added successfully");
            return "order-detail";
        }
    }
}
