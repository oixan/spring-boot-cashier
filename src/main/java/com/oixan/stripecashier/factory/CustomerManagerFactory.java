package com.oixan.stripecashier.factory;

import com.oixan.stripecashier.builder.StripeBuilder;
import com.oixan.stripecashier.interfaces.IUserStripe;
import com.oixan.stripecashier.manager.CustomerManager;

public class CustomerManagerFactory {

	public static CustomerManager create(IUserStripe user) {
		return new CustomerManager(new StripeBuilder(PropertiesFactory.create()))
				    	.setUser(user);
	}
}
