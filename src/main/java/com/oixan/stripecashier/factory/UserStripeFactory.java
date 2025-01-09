package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.manager.SubscriptionManager;
import com.oixan.stripecashier.proxy.UserStripeActionProxy;

/**
 * The {@code UserStripeFactory} class is responsible for creating instances of {@link IUserStripeAction}.
 * <p>
 * This factory encapsulates the creation of various dependencies required by {@link IUserStripeAction}
 * and returns a proxy implementation using {@link UserStripeActionProxy}.
 */
public class UserStripeFactory {

    /**
     * Default constructor for {@code UserStripeFactory}.
     */
    public UserStripeFactory() {
    }

    /**
     * Creates an {@link IUserStripeAction} instance for the given user model.
     * <p>
     * This method initializes and assembles the necessary components, including builders, managers,
     * and proxies, to create a complete {@link IUserStripeAction} instance.
     *
     * @param model The user model implementing {@link IUserStripe} interface
     * @return A proxied {@link IUserStripeAction} instance configured with the necessary dependencies
     */
    public static IUserStripeAction create(IUserStripe model) {
        // Initialize the CheckoutBuilder
        CheckoutBuilder checkoutBuilder = new CheckoutBuilder(
                new StripeBuilder(PropertiesFactory.create())
        );

        // Create the SubscriptionBuilder using the model
        SubscriptionBuilder subscriptionBuilder = SubscriptionBuilderFactory.create(model);

        // Create the SubscriptionManager for the model
        SubscriptionManager subscriptionManager = SubscriptionManagerFactory.create(model);

        // Create the CustomerManager for the model
        CustomerManager customerManager = CustomerManagerFactory.create(model);

        // Create the PaymentMethodsManager using the CustomerManager
        PaymentMethodsManager paymentMethodsManager = PaymentMethodsManagerFactory.create(customerManager);

        // Create and return the proxy implementation of IUserStripeAction
        return UserStripeActionProxy.createProxy(
                model,
                checkoutBuilder,
                subscriptionBuilder,
                subscriptionManager,
                customerManager,
                paymentMethodsManager
        );
    }
}
