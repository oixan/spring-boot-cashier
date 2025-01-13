package com.oixan.stripecashier.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;

/**
 * The class is responsible for creating an instance of
 * It abstracts the process of initializing the required dependencies for the 
 */
@Component
public class SubscriptionBuilderFactory {
	
	@Autowired
	SubscriptionBuilder subscriptionBuilder;
	
	@Autowired
	CustomerManagerFactory customerManagerFactory;
	
	@Autowired
	PaymentMethodsManagerFactory paymentMethodsManagerFactory;

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
    public SubscriptionBuilder create(IUserStripe user) {
        CustomerManager cm = customerManagerFactory.create(user);
        PaymentMethodsManager pm = paymentMethodsManagerFactory.create(user);
        
        // Return a new SubscriptionBuilder initialized with the required dependencies
        return subscriptionBuilder
        			.setCustomerManager(cm)
        			.setPaymentMethodsManager(pm);
    }
}
