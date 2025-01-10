package com.oixan.stripecashier.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

/**
 * The  class holds the configuration properties related to Stripe.
 */
@Configuration
@EnableConfigurationProperties(StripeProperties.class)
public class StripeAutoConfiguration {
  
  private StripeProperties stripeProperties;

  /**
   * The Stripe API key fetched from the application's configuration.
   */
  public StripeAutoConfiguration(StripeProperties stripeProperties) {
	  this.stripeProperties = stripeProperties;
  }
  
  
  @PostConstruct
  public void config() {
	  Stripe.apiKey = stripeProperties.getApiKey();
	  System.out.println("Stripe Api Key: SET");
  }
  
}
