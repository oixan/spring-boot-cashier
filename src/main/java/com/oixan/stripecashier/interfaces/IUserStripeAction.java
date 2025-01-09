package com.oixan.stripecashier.interfaces;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.manager.SubscriptionManager;

/**
 * Interface defining actions for a user interacting with the Stripe system.
 * Provides methods for handling checkout processes, subscriptions, customer management,
 * and payment methods.
 */
public interface IUserStripeAction {

    /**
     * Initiates a checkout process using a {@link CheckoutBuilder}.
     *
     * @return an instance of {@link CheckoutBuilder} to build and execute the checkout process.
     */
    CheckoutBuilder checkout();

    /**
     * Initiates a subscription process using a {@link SubscriptionBuilder}.
     *
     * @return an instance of {@link SubscriptionBuilder} to build and execute the subscription process.
     */
    SubscriptionBuilder subscribe();

    /**
     * Retrieves the current user's Stripe details.
     *
     * @return an instance of {@link IUserStripe} containing the user's Stripe information.
     */
    IUserStripe getUserStripe();

    /**
     * Provides access to subscription management functionalities.
     *
     * @return an instance of {@link SubscriptionManager} to manage subscriptions.
     */
    SubscriptionManager subscription();

    /**
     * Provides access to customer management functionalities.
     *
     * @return an instance of {@link CustomerManager} to manage customer data.
     */
    CustomerManager customer();

    /**
     * Provides access to payment methods management functionalities.
     *
     * @return an instance of {@link PaymentMethodsManager} to manage payment methods.
     */
    PaymentMethodsManager paymentMethod();
}
