package com.oixan.stripecashier.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * The  class holds the configuration properties related to Stripe.
 */
@Configuration
@EnableConfigurationProperties(StripeProperties.class)
public class StripeAutoConfiguration {
  

  /**
   * The Stripe API key fetched from the application's configuration.
   */
  public StripeAutoConfiguration(StripeProperties stripeProperties) {
   
  }
}
