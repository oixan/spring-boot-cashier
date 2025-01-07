package com.oixan.stripecashier.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import com.oixan.stripecashier.builder.StripeBuilder;

@Configuration
@ComponentScan(basePackages = "com.oixan.stripecashier.config")
@SpringBootTest(classes = StripePropertiesTest.class)
@TestPropertySource(locations = "classpath:application.properties")
public class StripePropertiesTest {

    @Test
    void testStripeApiKey() {
    	
    	String stripeApiKey = StripeProperties.getApiKey();
    	new StripeBuilder(StripeProperties.instance());
    	
        //assertEquals("test_key_12345", stripeApiKey, "La chiave API non è correttamente impostata!");
        
        //assertEquals("test_key_12345", Stripe.apiKey, "La chiave API di Stripe non è correttamente configurata!");
    }
}
