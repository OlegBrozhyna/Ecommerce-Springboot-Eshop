package com.eshop.customer.config;

import com.eshop.library.model.Customer;
import com.eshop.library.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;


public class CustomerServiceConfig implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    // This method is used to load user details by their username when authentication is required.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve a customer from the repository based on the provided username.
        Customer customer = customerRepository.findByUsername(username);

        // If no customer is found, throw a UsernameNotFoundException.
        if (customer == null) {
            throw new UsernameNotFoundException("Could not find username");
        }

        // Create and return a UserDetails object using the retrieved customer's data.
        return new User(
                customer.getUsername(),
                customer.getPassword(),
                customer.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()));
    }
}