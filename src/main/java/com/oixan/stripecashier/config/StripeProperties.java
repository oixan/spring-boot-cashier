package com.oixan.stripecashier.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The  class holds the configuration properties related to Stripe.
 * It retrieves the Stripe API key from the application properties and provides a getter method to access it.
 * 
 */
@ConfigurationProperties(prefix = "stripe")
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
    private String apiKey;

    /**
     * Gets the Stripe API key.
     * 
     * @return The Stripe API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API key for Stripe integration.
     *
     * @param apiKey the API key to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

}
