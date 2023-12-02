package com.eshop.customer.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

public interface CustomerConfigurations {
    // Configure the security filter chain and define security rules using HttpSecurity.
    void configure(HttpSecurity http) throws Exception;

    void configure(WebSecurity web) throws Exception;
}
