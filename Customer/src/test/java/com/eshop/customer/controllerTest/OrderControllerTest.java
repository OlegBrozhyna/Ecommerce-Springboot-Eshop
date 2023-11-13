package com.eshop.customer.controllerTest;

import com.eshop.customer.controller.OrderController;
import com.eshop.library.dto.CustomerDto;
import com.eshop.library.model.Customer;
import com.eshop.library.model.Order;
import com.eshop.library.model.ShoppingCart;
import com.eshop.library.repository.ShoppingCartRepository;
import com.eshop.library.service.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private OrderService orderService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private CountryService countryService;

    @Mock
    private CityService cityService;

    @Mock
    private Model model;
    @Mock
    private HttpSession session;
    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private OrderController orderController;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void testCheckOutAuthenticatedUserIncompleteInfo() {
        // Mocking
        Principal principal = () -> "testCustomer";
        when(customerService.getCustomer("testCustomer")).thenReturn(new CustomerDto());
        when(countryService.findAll()).thenReturn(new ArrayList<>());
        when(cityService.findAll()).thenReturn(new ArrayList<>());

        // Testing
        Model model = mock(Model.class);
        String viewName = orderController.checkOut(principal, model);

        // Verification
        verify(customerService, times(1)).getCustomer("testCustomer");
        verify(countryService, times(1)).findAll();
        verify(cityService, times(1)).findAll();
        verify(model, times(1)).addAttribute(eq("information"), eq("You need to update your information before check out"));
        verify(model, times(1)).addAttribute(eq("customer"), any());

        verify(model, times(1)).addAttribute(eq("cities"), anyList());
        verify(model, times(1)).addAttribute(eq("countries"), any(List.class));
        verify(model, times(1)).addAttribute(eq("title"), eq("Profile"));
        verify(model, times(1)).addAttribute(eq("page"), eq("Profile"));
        verifyNoMoreInteractions(customerService, countryService, cityService, model);

        // Assertions
        assert viewName != null;
        assert viewName.equals("customer-information");
    }

    @Test
    void testGetOrders() {
        // Mocking
        Principal principal = () -> "testCustomer";
        when(customerService.findByUsername("testCustomer")).thenReturn(new Customer());
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        // Testing
        String viewName = orderController.getOrders(model, principal);

        // Verification
        verify(customerService, times(1)).findByUsername("testCustomer");
        verify(model, times(1)).addAttribute(eq("orders"), any(List.class));
        verify(model, times(1)).addAttribute(eq("title"), eq("Order"));
        verify(model, times(1)).addAttribute(eq("page"), eq("Order"));
        verifyNoMoreInteractions(customerService, model);

        // Assertions
        assertEquals("order", viewName);
    }

    @Test
    void testCancelOrder() {
        // Mocking
        doNothing().when(orderService).cancelOrder(1L);
        when(redirectAttributes.addFlashAttribute(anyString(), any())).thenReturn(redirectAttributes);

        // Testing
        String viewName = orderController.cancelOrder(1L, redirectAttributes);

        // Verification
        verify(orderService, times(1)).cancelOrder(1L);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("success"), eq("Order canceled successfully!"));
        verifyNoMoreInteractions(orderService, redirectAttributes);

        // Assertions
        assertEquals("redirect:/orders", viewName);

        // Additional assertions
        verify(redirectAttributes, never()).addFlashAttribute(eq("someOtherAttribute"), any());

        // Verify the order of method invocations
        InOrder inOrder = inOrder(orderService, redirectAttributes);
        inOrder.verify(orderService).cancelOrder(1L);
        inOrder.verify(redirectAttributes).addFlashAttribute(eq("success"), eq("Order canceled successfully!"));
    }

    @Test
    void testCreateOrder() {
        // Mocking
        Principal principal = new UsernamePasswordAuthenticationToken("testUser", "password");
        Customer customer = new Customer();
        ShoppingCart cart = new ShoppingCart();

        // Mock the behavior of findByUsername
        when(customerService.findByUsername("testUser")).thenReturn(customer);

        // Set up the customer with the cart
        customer.setCart(cart);

        // Mock other service calls
        when(orderService.save(any())).thenReturn(new Order());

        // Testing
        String viewName = orderController.createOrder(principal, model, session);

        // Verification
        verify(session, times(1)).removeAttribute("totalItems");
        verify(model, times(1)).addAttribute(eq("order"), any(Order.class));
        verify(model, times(1)).addAttribute(eq("title"), eq("Order Detail"));
        verify(model, times(1)).addAttribute(eq("page"), eq("Order Detail"));
        verify(model, times(1)).addAttribute(eq("success"), eq("Order added successfully"));
        verifyNoMoreInteractions(customerService, orderService, model, session);

        // Assertions
        assertEquals("order-detail", viewName, "View name should be 'order-detail'");
    }
}

