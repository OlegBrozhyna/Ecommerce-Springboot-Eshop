package com.eshop.library.service.impl;

import com.eshop.library.model.*;
import com.eshop.library.repository.CustomerRepository;
import com.eshop.library.repository.OrderDetailRepository;
import com.eshop.library.repository.OrderRepository;
import com.eshop.library.service.OrderService;
import com.eshop.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    // Service implementation for handling orders

    private final OrderRepository orderRepository; // Repository for orders
    private final OrderDetailRepository detailRepository; // Repository for order details
    private final CustomerRepository customerRepository; // Repository for customers
    private final ShoppingCartService cartService; // Service for managing shopping carts

    @Override
    public Order save(ShoppingCart shoppingCart) {
        // Method to save an order based on the contents of a shopping cart

        Order order = new Order(); // Create a new order
        order.setOrderDate(new Date()); // Set the order date to the current date
        order.setCustomer(shoppingCart.getCustomer()); // Set the customer for the order
        order.setTax(2); // Set a default tax value (2%)
        order.setTotalPrice(shoppingCart.getTotalPrice()); // Set the total price of the order
        order.setAccept(false); // Set the order as not yet accepted
        order.setPaymentMethod("Cash and Credit Card"); // Set default payment method as "Cash and Credit Card"
        order.setOrderStatus("Pending"); // Set the order status as "Pending"
        order.setQuantity(shoppingCart.getTotalItems()); // Set the quantity based on items in the shopping cart
        List<OrderDetail> orderDetailList = new ArrayList<>(); // Initialize a list to hold order details
        for (CartItem item : shoppingCart.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail(); // Create an order detail for each item in the shopping cart
            orderDetail.setOrder(order); // Set the order for the order detail
            orderDetail.setProduct(item.getProduct()); // Set the product in the order detail
            detailRepository.save(orderDetail); // Save the order detail to the repository
            orderDetailList.add(orderDetail); // Add the order detail to the list
        }
        order.setOrderDetailList(orderDetailList); // Set the list of order details in the order
        cartService.deleteCartById(shoppingCart.getId()); // Delete the shopping cart after order creation
        return orderRepository.save(order); // Save the order to the repository and return it
    }

    @Override
    public List<Order> findAll(String username) {
        // Method to find all orders associated with a specific username

        Customer customer = customerRepository.findByUsername(username); // Find the customer by username
        List<Order> orders = customer.getOrders(); // Get the list of orders associated with the customer
        return orders; // Return the list of orders
    }

    @Override
    public List<Order> findALlOrders() {
        // Method to retrieve all orders

        return orderRepository.findAll(); // Return a list containing all orders
    }

    @Override
    public Order acceptOrder(Long id) {
        // Method to accept an order by its ID

        Order order = orderRepository.getById(id); // Retrieve the order by its ID
        order.setAccept(true); // Set the order status as accepted
        order.setDeliveryDate(new Date()); // Set the delivery date to the current date
        return orderRepository.save(order); // Save and return the updated order
    }

    @Override
    public void cancelOrder(Long id) {
        // Method to cancel an order by its ID

        orderRepository.deleteById(id); // Delete the order by its ID
    }
}
