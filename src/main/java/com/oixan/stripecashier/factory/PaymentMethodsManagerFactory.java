package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;

/**
 * The {@code PaymentMethodsManagerFactory} class is a factory for creating instances of {@link PaymentMethodsManager}.
 * It facilitates the creation of a {@link PaymentMethodsManager} by providing the necessary dependencies,
 * including a configured {@link StripeBuilder} and an existing {@link CustomerManager}.
 * 
 * <p>The factory method uses the {@link PropertiesFactory} to create the required properties for the {@link StripeBuilder},
 * which is then passed to the {@link PaymentMethodsManager}.
 */
public class PaymentMethodsManagerFactory {

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private PaymentMethodsManagerFactory() {
		// Private constructor to prevent instantiation
	}

    /**
     * Creates a new {@link PaymentMethodsManager} instance.
     * 
     * @param cm The {@link CustomerManager} instance that manages customer information
     * @return A newly created {@link PaymentMethodsManager} instance
     */
    public static PaymentMethodsManager create(CustomerManager cm) {
        return new PaymentMethodsManager(
                        new StripeBuilder(PropertiesFactory.create())
                    )
                    .setCustomerManager(cm);
    }
}
