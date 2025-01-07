package com.oixan.stripecashier.config;

import org.junit.jupiter.api.Test;

import com.oixan.stripecashier.BaseTest;
import com.oixan.stripecashier.builder.StripeBuilder;


public class StripePropertiesTest extends BaseTest {

    @Test
    void testStripeApiKey() {
    	
    	String stripeApiKey = StripeProperties.getApiKey();
    	new StripeBuilder(StripeProperties.instance());
    	
        //assertEquals("test_key_12345", stripeApiKey, "La chiave API non è correttamente impostata!");
        
        //assertEquals("test_key_12345", Stripe.apiKey, "La chiave API di Stripe non è correttamente configurata!");
    }
}
