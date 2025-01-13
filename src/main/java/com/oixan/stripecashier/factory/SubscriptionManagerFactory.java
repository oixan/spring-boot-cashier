package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.SubscriptionManager;

/**
 * The {@code SubscriptionManagerFactory} class is responsible for creating an instance of {@link SubscriptionManager}.
 * It abstracts the initialization process of the {@link SubscriptionManager} by setting the required user.
 * <p>
 * The factory class ensures that the {@link SubscriptionManager} is properly initialized with the given user
 * through the {@link SubscriptionManager#setUser(IUserStripe)} method.
 */
@Component
public class SubscriptionManagerFactory {
	
	@Autowired
	SubscriptionManager subscriptionManager;

    /**
     * Private constructor to prevent instantiation of this singleton class.
     */
    private SubscriptionManagerFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates an instance of {@link SubscriptionManager} for the specified {@link IUserStripe} user.
     * The created {@link SubscriptionManager} instance is initialized with the provided user.
     *
     * @param user The user for which the subscription manager is created
     * @return The {@link SubscriptionManager} instance initialized with the provided user
     */
    public SubscriptionManager create(IUserStripe user) {
        // Create and return a new SubscriptionManager initialized with the given user
        return subscriptionManager
                	.setUser(user);
    }
}
