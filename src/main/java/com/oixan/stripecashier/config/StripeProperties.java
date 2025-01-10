package com.oixan.stripecashier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The  class holds the configuration properties related to Stripe.
 * It retrieves the Stripe API key from the application properties and provides a getter method to access it.
 * 
 */
@Component
public class StripeProperties {

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the method.
	 */
	public StripeProperties() {
		// Private constructor to prevent instantiation
	}

    @Value("${stripe.apiKey}")
    private String apiKey;


    /**
     * Retrieves the API key used for authenticating with the Stripe service.
     *
     * @return the API key as a String.
     */
    public String getApiKey() {
        return apiKey;
    }
}
