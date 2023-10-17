package com.eshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// The annotation @SpringBootApplication indicates that this class is the entry point of a Spring Boot application.
// It scans for components, configurations, and services in the specified packages: "com.eshop.library.*" and "com.eshop.admin.*".

// @EnableJpaRepositories is used to enable JPA repository scanning. It specifies the base package for JPA repository interfaces,
// which is "com.eshop.library.repository". This is where Spring Data JPA will look for repository interfaces to generate repository beans.

// @EntityScan is used to specify the base package for entity classes. It tells Spring Boot where to scan for JPA entity classes,
// which is "com.eshop.library.model".
@SpringBootApplication(scanBasePackages = {"com.eshop.library.*", "com.eshop.admin.*"})
@EnableJpaRepositories(value = "com.eshop.library.repository")
@EntityScan(value = "com.eshop.library.model")
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
