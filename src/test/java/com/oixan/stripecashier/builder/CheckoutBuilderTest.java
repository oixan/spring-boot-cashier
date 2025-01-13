package com.oixan.stripecashier.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oixan.stripecashier.BaseTest;
import com.oixan.stripecashier.factory.CustomerManagerFactory;
import com.oixan.stripecashier.factory.PaymentMethodsManagerFactory;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;


public class CheckoutBuilderTest extends BaseTest {
  
	 private CustomerManager customerManager;
	 
	 @Autowired
	 private CustomerManagerFactory customerManagerFactory;
	 
	 @Autowired 
	 PaymentMethodsManagerFactory paymentMethodsManagerFactory;
	 
	 @Autowired
	 CheckoutBuilder checkoutBuilder;

	 @BeforeEach
	 protected void setUp() throws StripeException {
        super.setUp();

        StripeBuilder stripeBuilder = new StripeBuilder();
        customerManager = customerManagerFactory.create(userMock);
    }

	
	@Test
    void testCreateAsStripeCustomer() throws StripeException {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer chekcout");
        options.put("email", "checkout@live.it");
        String stripeId = customerManager
                            .createAsStripeCustomer(options);
                            
        assertNotNull(stripeId);

        // Step 2: Initialize PaymentMethodsManager and add a payment method
    	PaymentMethodsManager paymentMethodsManager = paymentMethodsManagerFactory.create(userMock);

    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = paymentMethodsManager.addPaymentMethod(firstPaymentMethodId);

    	// Step 3: Set the first payment method as the default
    	paymentMethodsManager.setDefaultPaymentMethod(firstPaymentMethod);


        // Step 4: Initialize CheckoutBuilder and create a checkout session
        String checkoutUrl = checkoutBuilder
                                    .setUser(userMock)
                                    .setPriceId("price_1QdxAQCtyihjMHctL68So9pU")
                                    .setQuantity(1)
                                    .setSuccessURL("https://example.com/success")
                                    .setCancelURL("https://example.com/cancel")
                                    .complete();

        assertNotNull(checkoutUrl);
        assertThat(checkoutUrl, startsWith("https://checkout.stripe.com/c/pay/"));
    }

}
