package com.eshop.admin.config;


import com.eshop.library.model.Admin;
import com.eshop.library.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//The UserDetails implementation in the AdminDetails class allows Spring Security to provide details about an administrator,
// including information about their roles, password, and account status.
// This is important for determining access rights and processing user authentication and authorization in the system.
public class AdminDetails implements UserDetails {

    // An object that contains information about the administrator
    private Admin admin;

    // Method that returns user roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Go through the roles assigned to the administrator
        for (Role role : admin.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    // Method that returns the user's password
    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    // Method that returns the username (login)
    @Override
    public String getUsername() {
        return admin.getUsername();
    }

    // Method that indicates if the user account is valid (always true in this case)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Method that indicates whether the user account is locked (always true in this case)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Method that indicates whether the user account has expired (always true in this case)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Method that indicates whether the user account is activated (always true in this case)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
