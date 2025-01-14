package com.oixan.stripecashier.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oixan.stripecashier.BaseTest;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionItem;


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
	
	
	@Test
    void testSubscriptionSwapItem() throws StripeException {
		// Step 1: Initialize SubscriptionBuilder and create a subscription
		Subscription subscriptionInitial = userActionMock.subscribe()
		                                                  .setPriceId("price_1JMEKICtyihjMHctwnT3KH9g")
		                                                  .start();

		// Step 2: Verify that the initial subscription is active
		assertNotNull(subscriptionInitial);
		assertEquals("active", subscriptionInitial.getStatus());

		// Step 3: Perform the item swap on the subscription
		Subscription stripeSubscriptionSwap = userActionMock.subscription()
		                                                      .swapItemSubscription("default", "price_1Qgpw4CtyihjMHctcKBNCy2e");

		// Step 4: Verify that the updated subscription is returned correctly
		assertNotNull(stripeSubscriptionSwap);

		// Step 5: Retrieve the updated subscription and verify that it is still active
		Subscription updatedSubscription = Subscription.retrieve(subscriptionInitial.getId());

		// Verify that the subscription is still active
		assertEquals("active", updatedSubscription.getStatus());

		// Step 6: Verify that the subscription item was updated with the new priceId
		boolean priceUpdated = false;
		List<SubscriptionItem> items = updatedSubscription.getItems().getData();
		for (SubscriptionItem item : items) {
		    if (item.getPrice().getId().equals("price_1Qgpw4CtyihjMHctcKBNCy2e")) {
		        priceUpdated = true;
		        break;
		    }
		}
		assertTrue(priceUpdated);

		// Step 7: Verify that the subscription has not ended in the database
		assertFalse(userActionMock.subscription().ended("default"));
    }

}
