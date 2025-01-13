package com.oixan.stripecashier.service.stripe;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oixan.stripecashier.entity.Subscription;
import com.oixan.stripecashier.factory.SubscriptionServiceFactory;
import com.oixan.stripecashier.factory.UserStripeFactory;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.service.SubscriptionService;

import com.stripe.exception.StripeException;

/**
 * Service class that demonstrates various Stripe-related actions.
 * This includes creating a Stripe customer, adding a payment method,
 * initiating a checkout, subscribing to a plan, and managing subscriptions.
 */
@Service
public class Test {
	
	@Autowired
	UserStripeFactory userStripeFactory;
	
	@Autowired
	SubscriptionServiceFactory subscriptionServiceFactory;

	/**
	 * Private constructor to prevent instantiation.
	 */
	private Test() {
	}

    /**
     * Runs a series of actions involving Stripe services such as creating a customer,
     * adding a payment method, completing checkout, subscribing to a plan, and managing subscriptions.
     * 
     * @throws StripeException If any error occurs while interacting with the Stripe API.
     */
    public void test() throws StripeException {
        UserTest user = new UserTest();
        IUserStripeAction userStripe = userStripeFactory.create(user);

        // Create a Stripe customer
        userStripe.customer()
                   .createAsStripeCustomer(null);

        // Add a payment method to the user's Stripe account
        userStripe.paymentMethod()
                   .addPaymentMethod("id");

        // Complete the checkout process
        userStripe.checkout()
                   .setPriceId("idprice")
                   .setQuantity(1)
                   .complete();

        // Start a subscription for the user
        userStripe.subscribe()
                   .setPriceId("idprice")
                   //.setTrialDay(30)
                   .start();

        // Retrieve and print subscription information
        SubscriptionService subscriptionService = subscriptionServiceFactory.create();
        Optional<Subscription> subscription = subscriptionService.getSubscriptionByUserIdAndType("id", "default");
        subscription.ifPresent(s -> System.out.println("Subscription found: " + s.getType()));

        // Cancel subscription at the end of the current period
        userStripe.subscription()
                   .cancelAtPeriodEnd("default");

        // Check if the subscription is on trial
        userStripe.subscription()
                   .onTrial("default");

        // Check if the subscription has ended
        userStripe.subscription()
                   .ended();
    }
}

/**
 * A test implementation of the IUserStripe interface.
 * This is used to simulate a user in the Stripe system for testing purposes.
 */
class UserTest implements IUserStripe {

    /**
     * A placeholder method for testing purposes.
     */
    public void ciao() {

    }

    @Override
    public String getStripeId() {
        return null; // Placeholder implementation
    }

    @Override
    public void setStripeId(String stripeId) {
        // Placeholder implementation
    }

    @Override
    public String getName() {
        return null; // Placeholder implementation
    }

    @Override
    public void setName(String name) {
        // Placeholder implementation
    }

    @Override
    public String getEmail() {
        return null; // Placeholder implementation
    }

    @Override
    public void setEmail(String email) {
        // Placeholder implementation
    }

    @Override
    public String getPhone() {
        return null; // Placeholder implementation
    }

    @Override
    public void setPhone(String phone) {
        // Placeholder implementation
    }

    @Override
    public String getPreferredLocales() {
        return null; // Placeholder implementation
    }

    @Override
    public void setPreferredLocales(String preferredLocales) {
        // Placeholder implementation
    }
}
