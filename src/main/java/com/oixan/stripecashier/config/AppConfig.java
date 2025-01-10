package com.oixan.stripecashier.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The {@code AppConfig} class serves as the primary configuration class for the application.
 * It sets up component scanning, JPA repository configuration, and entity scanning.
 * 
 * This class does not require any explicit constructor as Spring Boot uses the default constructor 
 * to instantiate it and configure the application context.
 */
@ComponentScan({"com.oixan.stripecashier"})
@EnableJpaRepositories({"com.oixan.stripecashier.repository"})
@EntityScan({"com.oixan.stripecashier.entity"})
//@SpringBootApplication
public class AppConfig {
    
    /**
     * Default constructor used by Spring Boot to instantiate and configure the application context.
     * No additional implementation is needed.
     */
    public AppConfig() {
        // Default constructor
    }
}
