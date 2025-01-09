package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;

/**
 * The {@code CustomerManagerFactory} class is a factory for creating instances of {@link CustomerManager}.
 * This factory method provides an easy way to create a {@link CustomerManager} instance
 * by passing in a {@link IUserStripe} user.
 * 
 * <p>The factory uses the {@link StripeBuilder} to build the required configuration for the manager,
 * leveraging the {@link PropertiesFactory} to create the necessary properties.
 */
public class CustomerManagerFactory {

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private CustomerManagerFactory() {
		// Private constructor to prevent instantiation
	}

    /**
     * Creates a new {@link CustomerManager} instance.
     * 
     * @param user The user who will interact with the Stripe customer manager
     * @return A newly created {@link CustomerManager} instance
     */
    public static CustomerManager create(IUserStripe user) {
        return new CustomerManager(new StripeBuilder(PropertiesFactory.create()))
                    .setUser(user);
    }
}
