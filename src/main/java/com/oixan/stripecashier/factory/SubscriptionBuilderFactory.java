package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;

/**
 * The class is responsible for creating an instance of
 * It abstracts the process of initializing the required dependencies for the 
 */
public class SubscriptionBuilderFactory {

    // Private constructor to prevent instantiation
    private SubscriptionBuilderFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates an instance of  using the specified  user.
     * It first creates a  and a for the user and then 
     * uses them to instantiate the .
     *
     * @param user The user for which the subscription builder is created
     * @return The  instance initialized with the provided user
     */
    public static SubscriptionBuilder create(IUserStripe user) {
        // Create the CustomerManager instance for the given user
        CustomerManager cm = CustomerManagerFactory.create(user);
        
        // Return a new SubscriptionBuilder initialized with the required dependencies
        return new SubscriptionBuilder(
                cm, 
                PaymentMethodsManagerFactory.create(cm)
        );
    }
}
