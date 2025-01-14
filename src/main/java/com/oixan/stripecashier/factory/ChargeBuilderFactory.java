package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.builder.ChargeBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;

/**
 * The {@code ChargeBuilderFactory} class is a factory for creating instances of   .
 * This factory method provides an easy way to create a    instance
 * by passing in a {@link IUserStripe} user.
 * 
 */
@Component
public class ChargeBuilderFactory {
	
	@Autowired
	ChargeBuilder chargeBuilder;

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private ChargeBuilderFactory() {
		// Private constructor to prevent instantiation
	}


    /**
     * Creates a new  instance.
     * 
     * @param user The user who will interact with the Stripe customer manager
     * @return A newly created instance
     */
    public ChargeBuilder create(IUserStripe user) {
        return chargeBuilder.setUser(user);
    }
}
