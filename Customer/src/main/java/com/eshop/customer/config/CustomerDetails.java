package com.eshop.customer.config;

import com.eshop.library.model.Customer;
import com.eshop.library.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class CustomerDetails implements UserDetails {
    private Customer customer;

    // This method returns a collection of granted authorities (roles) associated with the user.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : customer.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    // This method returns the user's password.
    @Override
    public String getPassword() {
        return customer.getPassword();
    }

    // This method returns the username (usually the user's unique identifier).
    @Override
    public String getUsername() {
        return customer.getUsername();
    }

    // These methods are used to determine if the user's account has expired, is locked, or if credentials have expired.
    // In this implementation, they always return true, indicating the account is always valid.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // This method indicates if the user is enabled (active). In this implementation, it always returns true.

    @Override
    public boolean isEnabled() {
        return true;
    }
}