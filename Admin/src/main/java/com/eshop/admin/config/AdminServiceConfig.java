package com.eshop.admin.config;

import com.eshop.library.model.Admin;
import com.eshop.library.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.stream.Collectors;

public class AdminServiceConfig implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;
    // This method loads a user by their username (login).
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find an admin by username in the repository.
        Admin admin = adminRepository.findByUsername(username);
        // If the admin is not found, throw a UsernameNotFoundException.
        if (admin == null) {
            throw new UsernameNotFoundException("Could not find username");
        }
        // Return a UserDetails object representing the admin.
        return new User(
                admin.getUsername(), // Username
                admin.getPassword(), // Password (usually hashed)
                admin.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
}
