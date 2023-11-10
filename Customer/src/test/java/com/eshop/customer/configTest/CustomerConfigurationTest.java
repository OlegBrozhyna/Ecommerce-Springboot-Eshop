package com.eshop.customer.configTest;

import com.eshop.customer.config.CustomerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerConfigurationTest {

    @Test
    void testUserDetailsServiceBean() {
        CustomerConfiguration customerConfiguration = new CustomerConfiguration();
        UserDetailsService userDetailsService = customerConfiguration.userDetailsService();
        assertNotNull(userDetailsService);
        // Add more assertions if needed
    }
    @Test
    void testPasswordEncoderBean() {
        CustomerConfiguration customerConfiguration = new CustomerConfiguration();
        BCryptPasswordEncoder passwordEncoder = customerConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        // Add more assertions if needed
    }
}
