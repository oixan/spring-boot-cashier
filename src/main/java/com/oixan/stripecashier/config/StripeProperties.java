package com.oixan.stripecashier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The  class holds the configuration properties related to Stripe.
 * It retrieves the Stripe API key from the application properties and provides a getter method to access it.
 * 
 */
@Configuration
public class StripeProperties {

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the method.
	 */
	public StripeProperties() {
		// Private constructor to prevent instantiation
	}

    /**
     * The Stripe API key fetched from the application's configuration.
     */
    @Value("${stripe.apiKey}")
    private String apiKey;

    /**
     * Gets the Stripe API key.
     * 
     * @return The Stripe API key
     */
    public String getApiKey() {
        return apiKey;
    }

}
