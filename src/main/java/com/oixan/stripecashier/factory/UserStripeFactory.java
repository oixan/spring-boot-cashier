package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.CheckoutBuilder;
import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.builder.SubscriptionBuilder;
import com.oixan.stripecashier.config.StripeProperties;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.interfaces.IUserStripeAction;
import com.oixan.stripecashier.manager.CustomerManager;
import com.oixan.stripecashier.manager.PaymentMethodsManager;
import com.oixan.stripecashier.manager.SubscriptionManager;
import com.oixan.stripecashier.proxy.UserStripeActionProxy;

public class UserStripeFactory{
		
	public UserStripeFactory() {
	}
	
	public static IUserStripeAction create(IUserStripe model)
	{
		CheckoutBuilder checkoutBuilder = new CheckoutBuilder(
				new StripeBuilder(StripeProperties.instance())
			);

		SubscriptionBuilder subscriptionBuilder = SubscriptionBuilderFactory.create(model);

		SubscriptionManager subscriptionManager = SubscriptionManagerFactory.create(model);

		CustomerManager customerManager = CustomerManagerFactory.create(model);

		PaymentMethodsManager paymentMethodsManager = PaymentMethodsManagerFactory.create(customerManager);
		
		return UserStripeActionProxy.createProxy(
														model, 
														checkoutBuilder, 
														subscriptionBuilder, 
														subscriptionManager,
														customerManager,
														paymentMethodsManager
													);	
	}

}