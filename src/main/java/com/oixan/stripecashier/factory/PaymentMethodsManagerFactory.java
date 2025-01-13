package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;

/**
 * The {@code PaymentMethodsManagerFactory} class is a factory for creating instances of {@link PaymentMethodsManager}.
 * It facilitates the creation of a {@link PaymentMethodsManager} by providing the necessary dependencies,
 * including a configured    and an existing   .
 * 
 * <p>The factory method uses the to create the required properties for the   ,
 * which is then passed to the {@link PaymentMethodsManager}.
 */
@Component
public class PaymentMethodsManagerFactory {
	
	@Autowired
	PaymentMethodsManager paymentMethodsManager;
	
	@Autowired
	CustomerManagerFactory customerManagerFactory;

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private PaymentMethodsManagerFactory() {
		// Private constructor to prevent instantiation
	}

    /**
     * Creates a {@link PaymentMethodsManager} for the specified user.
     *
     * @param user the {@link IUserStripe} instance representing the user
     * @return a configured {@link PaymentMethodsManager} instance
     */
    public PaymentMethodsManager create(IUserStripe user) {
    	CustomerManager cm = customerManagerFactory.create(user);
    	
        return paymentMethodsManager
                    .setCustomerManager(cm);
    }
}
