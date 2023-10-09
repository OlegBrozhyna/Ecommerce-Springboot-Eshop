package com.eshop.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AdminConfiguration {
    // Bean for configuring users and roles
    @Bean
    public UserDetailsService userDetailsService() {
        return new AdminServiceConfig();
    }

    // Bean for password encoding
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService())  // Using a custom userDetailsService
                .passwordEncoder(passwordEncoder());       // Using password encoding

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .authorizeHttpRequests()
                .requestMatchers("/*", "/static/**").permitAll()  // Allow public access to certain paths
                .requestMatchers("/admin/**").hasAuthority("ADMIN")  // Only users with the "ADMIN" role have access to /admin/**
                .anyRequest().authenticated()        // Require authentication for other requests
                .and()
                .formLogin()
                .loginPage("/login")  // Login page
                .loginProcessingUrl("/do-login") // URL for handling login
                .defaultSuccessUrl("/index", true) // URL after successful login
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID") // Delete Cookies
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Logout URL
                .logoutSuccessUrl("/login?logout") // URL after logout
                .permitAll()
                .and()
                .authenticationManager(authenticationManager);   // Set a custom AuthenticationManager
        return http.build();
    }
}