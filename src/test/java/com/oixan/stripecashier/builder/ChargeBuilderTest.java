package com.oixan.stripecashier.builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oixan.stripecashier.BaseTest;
import com.oixan.stripecashier.factory.ChargeBuilderFactory;
import com.oixan.stripecashier.factory.CustomerManagerFactory;
import com.oixan.stripecashier.factory.PaymentMethodsManagerFactory;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Refund;


public class ChargeBuilderTest extends BaseTest {
  
	 private CustomerManager customerManager;

     private ChargeBuilder chargeBuilder;

     @Autowired
     private ChargeBuilderFactory chargeBuilderFactory;
	 
	 @Autowired 
	 PaymentMethodsManagerFactory paymentMethodsManagerFactory;
	 
	 @Autowired
     CustomerManagerFactory customerManagerFactory;


	@BeforeEach
	 protected void setUp() throws StripeException {
        super.setUp();

        chargeBuilder = chargeBuilderFactory.create(userMock);
        customerManager = customerManagerFactory.create(userMock);
    }

	
	@Test
    void testCreateAChargeToUser() throws Exception {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer charge");
        options.put("email", "charge@live.it");
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
        PaymentIntent paymentIntent = userActionMock.charge()
                                                .pay(14.99);

        // Assert that the PaymentIntent was created successfully
        assertNotNull(paymentIntent);
        assertEquals(1499, paymentIntent.getAmount());  // Check if the amount is in cents
        assertEquals("usd", paymentIntent.getCurrency()); // Assuming USD as the default currency
        assertTrue(paymentIntent.getStatus().equals("requires_confirmation") || paymentIntent.getStatus().equals("succeeded"));                                 
    }


    @Test
    void testCreateAChargeAndRefundToUser() throws Exception {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test customer charge and refaund");
        options.put("email", "chargeRefaund@live.it");
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

        // Step 4: Create the charge
        PaymentIntent paymentIntent = userActionMock.charge()
                                                    .pay(25.99);

        // Step 5: Test refund
        Refund refund = userActionMock.charge()
                                    .refund(paymentIntent.getId());

        // Assert that the refund was created successfully
        assertNotNull(refund);
        assertEquals(paymentIntent.getId(), refund.getPaymentIntent());
        assertEquals("succeeded", refund.getStatus());
    }

}
