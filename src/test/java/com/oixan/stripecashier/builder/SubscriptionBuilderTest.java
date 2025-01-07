package com.oixan.stripecashier.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oixan.stripecashier.BaseTest;
import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.factory.PropertiesFactory;
import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;

public class SubscriptionBuilderTest  extends BaseTest {
  
	 private CustomerManager customerManager;

	 @BeforeEach
	protected void setUp() throws StripeException {
        super.setUp();

        StripeBuilder stripeBuilder = new StripeBuilder(PropertiesFactory.create());
        customerManager = new CustomerManager(stripeBuilder);
        customerManager.setUser(userMock);
    }
	 
	 
	@Test
    void testCreateSubscriptionExistingCustomerAndDefaultPaymentMethod() throws StripeException {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test existing customer new subscription");
        options.put("email", "subscription@live.it");
        String stripeId = customerManager
                                .createAsStripeCustomer(options);
        assertNotNull(stripeId);

        // Step 2: Initialize PaymentMethodsManager and add a payment method
    	PaymentMethodsManager paymentMethodsManager = new PaymentMethodsManager(new StripeBuilder(PropertiesFactory.create()))
    	    .setCustomerManager(customerManager);

    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = paymentMethodsManager.addPaymentMethod(firstPaymentMethodId);

    	// Step 3: Set the first payment method as the default
    	paymentMethodsManager.setDefaultPaymentMethod(firstPaymentMethod);


        // Step 4: Initialize SubscriptionBuilder and create a subscription
        IUserStripeAction userStripe = UserStripeFactory.create(userMock);
        Subscription subscriptionStripe = userStripe.subscribe()
									                  .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
									                  .start( null, null, null);

        assertNotNull(subscriptionStripe);                                              
       

        Optional<com.oixan.stripecashier.entity.Subscription> foundSubscription = SubscriptionServiceFactory.create().getSubscriptionByUserIdAndType(stripeId, "default");
        assertTrue(foundSubscription.isPresent());
        assertEquals(subscriptionStripe.getId(), foundSubscription.get().getStripeId());
    }


    @Test
    void testCreateSubscriptionNewCustomerAndNewPaymentMethod() throws StripeException {
        // Step 1: Initialize PaymentMethodsManager and add a payment method
        Map<String, Object> options = new HashMap<>();
        options.put("description", "Test new customer with new subscription");
        options.put("email", "subscription@live.it");

        // Define a test payment method ID (e.g., a Visa card ID)
        String firstPaymentMethodId = "pm_card_visa";

        // Step 2: Initialize SubscriptionBuilder and create a new user and new subscription
        IUserStripeAction userStripe = UserStripeFactory.create(userMock);
        Subscription subscriptionStripe = userStripe.subscribe()
                                                .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
                                                .startAndCreateStripeUserAndPaymentMethod(
                                                    options, null, firstPaymentMethodId, "default");
        
        assertNotNull(subscriptionStripe);

        Optional<com.oixan.stripecashier.entity.Subscription> 
                    foundSubscription = SubscriptionServiceFactory
                                                .create()
                                                .getSubscriptionByUserIdAndType(
                                                    userStripe.getUserStripe().getStripeId(), "default");
                                                    
        assertTrue(foundSubscription.isPresent());
        assertEquals(subscriptionStripe.getId(), foundSubscription.get().getStripeId());
    }

}
