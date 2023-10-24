package com.eshop.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class CustomerConfiguration {

    // Define a custom UserDetailsService bean, which is used for loading user details.
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomerServiceConfig();
    }

    // Define a BCryptPasswordEncoder bean for password encoding.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure the security filter chain and define security rules using HttpSecurity.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Configure the AuthenticationManager to use the custom UserDetailsService and password encoder.
        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .authorizeRequests()
                .requestMatchers("/*", "/js/**", "/css/**", "/images/**", "/webfonts/**").permitAll() // Allow access to these resources without authentication
                .requestMatchers("/customer/**").hasAuthority("CUSTOMER") // Require "CUSTOMER" authority to access URLs under /customer
                .and()
                .formLogin()
                .loginPage("/login") // Define the login page URL
                .loginProcessingUrl("/do-login") // URL for processing login requests
                .defaultSuccessUrl("/index", true) // Redirect to "/index" after successful login
                .permitAll() // Allow access to login-related URLs without authentication
                .and()
                .logout()
                .deleteCookies("JSESSIONID")// Delete the JSESSIONID cookie
                .invalidateHttpSession(true) // Invalidate the user's session upon logout
                .clearAuthentication(true) // Clear the user's authentication upon logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Define the logout URL
                .logoutSuccessUrl("/login?logout") // Redirect to "/login?logout" after successful logout
                .permitAll() // Allow access to the logout URL without authentication
                .and()
                .authenticationManager(authenticationManager) // Set the custom authentication manager
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS); // Set session creation policy to "ALWAYS"

        return http.build(); // Build and return the configured HttpSecurity object
    }
}
