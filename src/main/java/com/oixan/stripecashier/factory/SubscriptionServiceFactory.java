package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.service.SubscriptionService;
import com.oixan.stripecashier.singleton.ApplicationContextSingleton;

/**
 * The {@code SubscriptionServiceFactory} class is responsible for creating and providing a single instance of 
 * {@link SubscriptionService}.
 * <p>
 * This class uses the singleton pattern to ensure that only one instance of the {@link SubscriptionService} 
 * is created and used throughout the application. The instance is retrieved from the Spring application context 
 * via the {@link ApplicationContextSingleton}.
 */
public class SubscriptionServiceFactory {
    
  
    // The cached instance of the SubscriptionService
    private static SubscriptionService subscriptionService;

    /**
     * Private constructor to prevent instantiation of this singleton class.
     * @return
     */
    private SubscriptionServiceFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the {@link SubscriptionService} instance from the application context, creating it if necessary.
     * <p>
     * If the instance of {@link SubscriptionService} has already been created, it will be returned directly.
     * Otherwise, it will be retrieved from the Spring application context via the {@link ApplicationContextSingleton}.
     *
     * @return The {@link SubscriptionService} instance
     */
    public static SubscriptionService create() {
        // Return the existing SubscriptionService if already created
        if (subscriptionService != null) {
            return subscriptionService;
        }

        // Create and cache the SubscriptionService instance from the Spring application context
        subscriptionService = ApplicationContextSingleton
                .create()
                .getBean(SubscriptionService.class);

        return subscriptionService;
    }
}
