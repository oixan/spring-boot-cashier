package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;

/**
 * The {@code CustomerManagerFactory} class is a factory for creating instances of   .
 * This factory method provides an easy way to create a    instance
 * by passing in a {@link IUserStripe} user.
 * 
 * <p>The factory uses the    to build the required configuration for the manager,
 * leveraging the to create the necessary properties.
 */
@Component
public class CustomerManagerFactory {
	
	@Autowired
	CustomerManager customerManager;

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private CustomerManagerFactory() {
		// Private constructor to prevent instantiation
	}

    /**
     * Creates a new    instance.
     * 
     * @param user The user who will interact with the Stripe customer manager
     * @return A newly created    instance
     */
    public CustomerManager create(IUserStripe user) {
        return customerManager.setUser(user);
    }
}
