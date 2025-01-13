package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.service.SubscriptionService;

/**
 * The {@code SubscriptionServiceFactory} class is responsible for creating and providing a single instance of 
 * {@link SubscriptionService}.
 * <p>
 * This class uses the singleton pattern to ensure that only one instance of the {@link SubscriptionService} 
 * is created and used throughout the application. The instance is retrieved from the Spring application context 
 * via the {@link ApplicationContextSingleton}.
 */
@Component
public class SubscriptionServiceFactory {
    
	@Autowired
    // The cached instance of the SubscriptionService
    private SubscriptionService subscriptionService;

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
    public SubscriptionService create() {
        return subscriptionService;
    }
}
