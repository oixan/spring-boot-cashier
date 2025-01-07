package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oixan.stripecashier.BaseTest;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;


public class SubscriptionManagerTest extends BaseTest {
  
    @BeforeEach
	protected void setUp() throws StripeException {
        super.setUp();

        userActionMock.customer().createAsStripeCustomer(options);

        // Step 1: Initialize PaymentMethodsManager and add a payment method
    	// Define a test payment method ID (e.g., a Visa card ID)
    	String firstPaymentMethodId = "pm_card_visa";

    	// Add the payment method and assert it is successfully added
    	PaymentMethod firstPaymentMethod = userActionMock.paymentMethod().addPaymentMethod(firstPaymentMethodId);

    	// Step 2: Set the first payment method as the default
    	userActionMock.paymentMethod().setDefaultPaymentMethod(firstPaymentMethod);
    }

	@Test
    void testSubscriptionCancelAtPeriodEndDefaultType() throws StripeException {
        
        // Step 1: Initialize SubscriptionBuilder and create a subscription
        userActionMock.subscribe()
                    .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
                    .start();
       
        // Step 2: Delete the subscription
        Subscription  stripeSubscriptionCancelled = userActionMock.subscription()
                                                                .cancelAtPeriodEnd();

        assertNotNull(stripeSubscriptionCancelled);
        assertTrue(stripeSubscriptionCancelled.getCancelAtPeriodEnd());
        assertTrue(userActionMock.subscription().onGracePeriod());
        assertFalse(userActionMock.subscription().ended());
    }

}
