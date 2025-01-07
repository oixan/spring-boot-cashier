package com.oixan.stripecashier.config;

import org.junit.jupiter.api.Test;

import com.oixan.stripecashier.BaseTest;
import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.factory.PropertiesFactory;


public class StripePropertiesTest extends BaseTest {

    @Test
    void testStripeApiKey() {
    	
    	String stripeApiKey = PropertiesFactory.create().getApiKey();
    	new StripeBuilder(PropertiesFactory.create());
    	
        //assertEquals("test_key_12345", stripeApiKey, "La chiave API non è correttamente impostata!");
        
        //assertEquals("test_key_12345", Stripe.apiKey, "La chiave API di Stripe non è correttamente configurata!");
    }
}
