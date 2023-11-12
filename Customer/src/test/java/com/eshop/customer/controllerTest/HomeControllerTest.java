package com.eshop.customer.controllerTest;

import com.eshop.customer.controller.HomeController;
import com.eshop.library.model.Customer;
import com.eshop.library.model.ShoppingCart;
import com.eshop.library.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    }

    @Test
    void testHome() {
        // Arrange
        // Create an instance of the HomeController with the mocked CustomerService
        HomeController controller = new HomeController(customerService);

        // Create mock objects for Model, HttpSession, and Principal
        Model model = Mockito.mock(Model.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        Principal principal = Mockito.mock(Principal.class);

        // Create a mock Customer with some sample data
        Customer mockCustomer = new Customer();
        mockCustomer.setFirstName("John");
        mockCustomer.setLastName("Doe");
        mockCustomer.setCart(new ShoppingCart());

        // Mock the customerService method to return the mockCustomer when findByUsername is called with null
        when(customerService.findByUsername(eq(null))).thenReturn(mockCustomer);

        // Act
        // Call the home method on the controller
        String viewName = controller.home(model, principal, session);

        // Assert
        // Verify that the view name is "home"
        assertEquals("home", viewName);

        // Verify that specific attributes are added to the model
        verify(model).addAttribute("title", "Home");
        verify(model).addAttribute("page", "Home");

        // Verify that the session attributes are set correctly
        verify(session).setAttribute("username", "John Doe");
        verify(session).setAttribute("totalItems", mockCustomer.getCart().getTotalItems());
    }

    @Test
    void testContact() {
        // Arrange
        // Create an instance of the HomeController with the mocked CustomerService
        HomeController controller = new HomeController(customerService);

        // Create a mock object for Model
        Model model = Mockito.mock(Model.class);

        // Act
        // Call the contact method on the controller
        String viewName = controller.contact(model);

        // Assert
        // Verify that the view name is "contact-us"
        assertEquals("contact-us", viewName);

        // Verify that specific attributes are added to the model
        verify(model).addAttribute("title", "Contact");
        verify(model).addAttribute("page", "Contact");
    }
}