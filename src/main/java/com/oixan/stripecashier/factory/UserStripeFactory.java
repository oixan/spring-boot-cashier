package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.builder.ChargeBuilder;
import com.oixan.stripecashier.builder.CheckoutBuilder;
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
@Component
public class UserStripeFactory {
	
	@Autowired
	SubscriptionBuilderFactory subscriptionBuilderFactory;
	
	@Autowired
	CheckoutBuilder checkoutBuilder;
	
	@Autowired
	SubscriptionManagerFactory subscriptionManagerFactory;
	
	@Autowired
	CustomerManagerFactory customerManagerFactory;
	
	@Autowired
	PaymentMethodsManagerFactory paymentMethodsManagerFactory;

    @Autowired
    ChargeBuilderFactory chargeBuilderFactory;

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
    public IUserStripeAction create(IUserStripe model) {
    	
    	checkoutBuilder.setUser(model);

        // Create the SubscriptionBuilder using the model
        SubscriptionBuilder subscriptionBuilder = subscriptionBuilderFactory.create(model);

        // Create the SubscriptionManager for the model
        SubscriptionManager subscriptionManager = subscriptionManagerFactory.create(model);

        // Create the CustomerManager for the model
        CustomerManager customerManager = customerManagerFactory.create(model);

        // Create the PaymentMethodsManager using the CustomerManager
        PaymentMethodsManager paymentMethodsManager = paymentMethodsManagerFactory.create(model);

        
        ChargeBuilder chargeBuilder = chargeBuilderFactory.create(model);

        // Create and return the proxy implementation of IUserStripeAction
        return UserStripeActionProxy.createProxy(
                model,
                checkoutBuilder,
                subscriptionBuilder,
                subscriptionManager,
                customerManager,
                paymentMethodsManager,
                chargeBuilder
        );
    }
}
