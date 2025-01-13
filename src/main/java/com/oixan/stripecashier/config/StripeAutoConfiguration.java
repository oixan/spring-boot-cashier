package com.oixan.stripecashier.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

/**
 * The  class holds the configuration properties related to Stripe.
 */
@Configuration
@EnableConfigurationProperties(StripeProperties.class)
@ComponentScan({"com.oixan.stripecashier"})
@EnableJpaRepositories({"com.oixan.stripecashier.repository"})
@EntityScan({"com.oixan.stripecashier.entity"})
//@SpringBootApplication
public class StripeAutoConfiguration {
  
  @Autowired
  private StripeProperties stripeProperties;

  /**
   * The Stripe API key fetched from the application's configuration.
   */
  public StripeAutoConfiguration() {
  }
  
  
  /**
   * Configures the Stripe API key.
   */
  @PostConstruct
  public void config() {
	  Stripe.apiKey = stripeProperties.getApiKey();
  }
  
}