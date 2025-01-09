package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.singleton.ApplicationContextSingleton;

/**
 * The {@code PropertiesFactory} class is responsible for creating and managing the {@link StripeProperties} instance.
 * It ensures that the {@link StripeProperties} is created lazily and can be accessed globally.
 * <p>
 * The factory uses the {@link ApplicationContextSingleton} to retrieve the {@link StripeProperties} bean from the application context.
 */
public class PropertiesFactory {

    /**
     * The {@link StripeProperties} instance.
     */
    private static StripeProperties properties;

    /**
	 * Private constructor to prevent instantiation of this singleton class.
	 * This class should only be used through the {@link #create()} method.
	 */
	private PropertiesFactory() {
		// Private constructor to prevent instantiation
	}

    /**
     * Creates and returns the {@link StripeProperties} instance.
     * If the properties have already been created, the existing instance will be returned.
     * 
     * @return The {@link StripeProperties} instance
     */
    public static StripeProperties create() {
        // If properties have already been created, return the existing instance
        if (properties != null) {
            return properties;
        }

        // Create the properties instance using the application context
        properties = ApplicationContextSingleton
                                            .create()
                                            .getBean(StripeProperties.class);

        return properties;
    }
}
