package com.eshop.customer.controllerTest;

import com.eshop.customer.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private HomeController controller;

    // This test verifies that the context loads successfully and the home page contains "Home" and "Login" strings.
    @Test
    void contextLoads() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home")))
                .andExpect(content().string(containsString("Login")));
    }

    // This test simulates an authenticated user and checks if accessing '/account' redirects to the login page.
    @Test
    void loginTest() throws Exception {
        // Simulating an authenticated user
        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("user@gmail.com", "12345");
        SecurityContextHolder.getContext().setAuthentication(principal);

        // Making the request to the '/account' endpoint
        mockMvc.perform(get("/account"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
